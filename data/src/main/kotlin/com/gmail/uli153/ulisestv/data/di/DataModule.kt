package com.gmail.uli153.ulisestv.data.di

import androidx.room.Room
import com.gmail.uli153.ulisestv.core.Constants
import com.gmail.uli153.ulisestv.core.DefaultDispatcherProvider
import com.gmail.uli153.ulisestv.core.DispatcherProvider
import com.gmail.uli153.ulisestv.data.error.RemoteCallHandler
import com.gmail.uli153.ulisestv.data.local.security.SecureTokenStorage
import com.gmail.uli153.ulisestv.data.local.security.TokenStorage
import com.gmail.uli153.ulisestv.data.remote.AuthInterceptor
import com.gmail.uli153.ulisestv.data.session.SessionManager
import com.gmail.uli153.ulisestv.data.session.SessionManagerImpl
import com.gmail.uli153.ulisestv.data.local.datasource.CategoryLocalDataSource
import com.gmail.uli153.ulisestv.data.local.datasource.CategoryLocalDataSourceImpl
import com.gmail.uli153.ulisestv.data.local.datasource.VideoLocalDataSource
import com.gmail.uli153.ulisestv.data.local.datasource.VideoLocalDataSourceImpl
import com.gmail.uli153.ulisestv.data.local.db.UlisesTvDatabase
import com.gmail.uli153.ulisestv.data.remote.api.CategoryApi
import com.gmail.uli153.ulisestv.data.remote.api.UserApi
import com.gmail.uli153.ulisestv.data.remote.api.VideoApi
import com.gmail.uli153.ulisestv.data.repository.CategoryRepositoryImpl
import com.gmail.uli153.ulisestv.data.repository.UserRepositoryImpl
import com.gmail.uli153.ulisestv.data.repository.VideoRepositoryImpl
import com.gmail.uli153.ulisestv.domain.repository.CategoryRepository
import com.gmail.uli153.ulisestv.domain.repository.UserRepository
import com.gmail.uli153.ulisestv.domain.repository.VideoRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

    // Remote data sources are flavor-specific (real vs mock): the matching
    // `remoteModule` comes from src/prod or src/mock.
    includes(remoteModule)

    // ---- Shared ----
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<TokenStorage> { SecureTokenStorage(androidContext()) }
    single<SessionManager> { SessionManagerImpl(get(), get(), get()) }

    // ---- Networking ----
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
        }
    }
    single { AuthInterceptor(get()) }
    single {
        OkHttpClient.Builder()
            // The session token is attached to every request here.
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    single { Moshi.Builder().build() }
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single { get<Retrofit>().create(UserApi::class.java) }
    single { get<Retrofit>().create(CategoryApi::class.java) }
    single { get<Retrofit>().create(VideoApi::class.java) }

    // ---- Room (local cache) ----
    single {
        Room.databaseBuilder(
            androidContext(),
            UlisesTvDatabase::class.java,
            Constants.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
    single { get<UlisesTvDatabase>().categoryDao() }
    single { get<UlisesTvDatabase>().videoDao() }

    // ---- Local data sources (remote ones are provided by the flavor's remoteModule) ----
    single<CategoryLocalDataSource> { CategoryLocalDataSourceImpl(get()) }
    single<VideoLocalDataSource> { VideoLocalDataSourceImpl(get()) }

    // ---- Repositories ----
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single { RemoteCallHandler(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get(), get(), get()) }
    single<VideoRepository> { VideoRepositoryImpl(get(), get(), get(), get()) }
}
