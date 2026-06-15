plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gmail.uli153.ulisestv.data"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }

    flavorDimensions += "environment"
    productFlavors {
        create("prod") { dimension = "environment" }
        create("mock") { dimension = "environment" }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Data layer depends on domain (and core for shared utilities).
    api(project(":domain"))
    implementation(project(":core"))

    implementation(libs.kotlinx.coroutines.android)

    // Retrofit + OkHttp + Moshi
    implementation(libs.bundles.network)
    ksp(libs.moshi.kotlin.codegen)

    // Room cache
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // Koin (for the data DI module)
    implementation(libs.koin.android)
}
