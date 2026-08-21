# UlisesTV

App de streaming de vídeo para **Android TV**, construida con **Compose for TV** y **Clean
Architecture** repartida en cuatro módulos Gradle, con capa de dominio en Kotlin puro,
caché offline con Room y almacenamiento de sesión cifrado en el Android Keystore.

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)
![Compose for TV](https://img.shields.io/badge/Compose%20for%20TV-tv--material-4285F4?logo=jetpackcompose&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-4%20m%C3%B3dulos-1F3A5F)
![minSdk](https://img.shields.io/badge/minSdk-31-3DDC84?logo=android&logoColor=white)
![Koin](https://img.shields.io/badge/DI-Koin%204-orange)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

<!-- TODO: añadir aquí 2-3 capturas del emulador de Android TV (Home, Detalle, Player).
     Un GIF corto de la navegación con el D-pad es lo que más vende en un repo de TV. -->

---

## Qué hace

Un catálogo de vídeo pensado para el sofá: login, exploración por categorías, buscador,
detalle de contenido y reproductor a pantalla completa, todo navegable con el mando a
distancia (D-pad) usando los componentes de foco de `androidx.tv`.

| Pantalla | Contenido |
|---|---|
| Login | Autenticación con usuario/contraseña y arranque de sesión |
| Home | Carruseles de contenido destacado |
| Categorías | Navegación del catálogo por categoría |
| Vídeos | Listado por categoría con carga de imágenes vía Coil |
| Detalle | Ficha del contenido y acceso a reproducción |
| Búsqueda | Búsqueda sobre categorías y vídeos |
| Player | Reproducción con ExoPlayer (Media3) |

## Lo que este repositorio demuestra

Más allá de la funcionalidad, estas son las decisiones técnicas que merece la pena revisar:

- **La capa de dominio no conoce Android.** `:core` y `:domain` usan el plugin
  `kotlin-jvm`, no `android-library`. No pueden importar `android.*` ni siquiera por
  accidente: la regla de dependencias la impone el sistema de build, no la disciplina.
- **Sesión cifrada con clave que nunca sale del Keystore.** `KeystoreCryptoManager` cifra
  los tokens con AES-256-GCM usando una clave generada en el Android Keystore
  (respaldada por hardware si el dispositivo lo soporta), en lugar de la ya obsoleta
  librería *Jetpack Security Crypto* / `EncryptedSharedPreferences`.
- **Renovación de sesión transparente.** `AuthInterceptor` + `SessionManager` +
  `TokenStorage` inyectan el access token y renuevan con el refresh token sin que las
  capas superiores se enteren.
- **Flavor `mock` instalable en paralelo.** El intercambio entre datos reales y simulados
  se hace con *source sets* de Gradle (`data/src/mock` y `data/src/prod`), cada uno con su
  propio `RemoteModule` de Koin. No hay un solo `if (BuildConfig.DEBUG)` en el código de
  producción.
- **Offline-first.** Room cachea catálogo y vídeos; los repositorios exponen `Flow` desde
  la base de datos y refrescan contra la red en segundo plano.
- **Errores como valor, no como excepción.** `Resource` y `RemoteError` en `:core`, con
  `RemoteCallHandler`/`RemoteErrorMapper` traduciendo fallos HTTP a errores de dominio.

## Arquitectura

```
        ┌──────────────────────────────────────────────────────┐
        │ :app          Compose for TV · ViewModels            │
        │               Navigation Compose · Koin (UI)         │
        └───────────────────────┬──────────────────────────────┘
                                │ depende de
        ┌───────────────────────▼──────────────────────────────┐
        │ :domain       modelos · interfaces de repositorio    │
        │  (kotlin-jvm) casos de uso                           │
        └───────────────────────▲──────────────────────────────┘
                                │ implementa
        ┌───────────────────────┴──────────────────────────────┐
        │ :data         Retrofit/Moshi · Room · Keystore       │
        │ (android-lib) DTOs · mappers · datasources · sesión  │
        └──────────────────────────────────────────────────────┘

        ┌──────────────────────────────────────────────────────┐
        │ :core         Resource · RemoteError                 │
        │  (kotlin-jvm) DispatcherProvider · Flow extensions   │
        └──────────────────────────────────────────────────────┘
```

`:app` habla con `:domain`; `:data` implementa sus interfaces y se cablea por inyección de
dependencias. La UI nunca ve un DTO ni una entidad de Room.

## Estructura del proyecto

```
ulisestv/
├── app/                        # Compose for TV, ViewModels, navegación, DI de UI
│   └── ui/{home,categories,videos,detail,search,player,login,navigation,theme}
├── core/                       # Kotlin puro: Resource, RemoteError, DispatcherProvider
├── domain/                     # Kotlin puro: modelos, repositorios (interfaces), use cases
│   └── usecase/                # Login, Logout, RefreshVideos, SearchVideo, ...
└── data/
    ├── src/main/               # DTOs, mappers, Retrofit APIs, Room, sesión y seguridad
    │   ├── local/{dao,db,datasource,security}
    │   ├── remote/{api,datasource}, AuthInterceptor
    │   └── repository/         # implementaciones de los repositorios de :domain
    ├── src/prod/               # RemoteModule -> data sources reales
    └── src/mock/               # RemoteModule -> data sources en memoria + DtoMocks
```

## Stack

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.2.10 (JVM 17) |
| UI | Jetpack Compose + `androidx.tv:tv-material` (Compose for TV) |
| Navegación | Navigation Compose |
| Asincronía | Coroutines + Flow |
| Inyección de dependencias | Koin 4 |
| Red | Retrofit + OkHttp + Moshi |
| Persistencia | Room 2.7 (KSP) |
| Imágenes | Coil |
| Vídeo | Media3 / ExoPlayer |
| Build | AGP 9.2, Gradle Version Catalogs (TOML), KSP |

## Cómo ejecutarlo

Requisitos: **Android Studio** reciente, **JDK 17** y un emulador o dispositivo
**Android TV con API 31+**.

```bash
git clone https://github.com/ulisescervera/ulisestv.git
cd ulisestv
```

El flavor **`mock` es el que funciona sin backend** y es el recomendado para probar la app:

```bash
./gradlew installMockDebug
```

```
Android Studio → selector de Build Variants → mockDebug → Run
```

> El flavor `prod` apunta a `Constants.BASE_URL` (`https://api.ulisestv.com/`), que es un
> endpoint de referencia y no un servicio publicado. Para usarlo, sustituye esa constante
> por tu propio backend. `mock` se instala con el sufijo `.mock`, así que ambos flavors
> pueden convivir en el mismo dispositivo.

## Pendiente

Trabajo consciente que aún no está hecho, por orden de prioridad:

- [ ] Tests unitarios de casos de uso y mappers — el diseño ya lo permite sin instrumentación,
      porque `:domain` y `:core` son módulos JVM puros.
- [ ] Activar R8 en `release` (`isMinifyEnabled = true`) y afinar las reglas de ProGuard.
- [ ] Migraciones de Room (hoy la base de datos está en `version = 1` con `exportSchema = false`).
- [ ] Baseline Profiles para mejorar el arranque en dispositivos de TV, habitualmente modestos.
- [ ] Tests de UI de navegación por D-pad y gestión del foco.

## Licencia

MIT — ver [LICENSE](LICENSE).
