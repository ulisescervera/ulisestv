package com.gmail.uli153.ulisestv.data.di

import com.gmail.uli153.ulisestv.data.remote.datasource.CategoryRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.CategoryRemoteDataSourceImpl
import com.gmail.uli153.ulisestv.data.remote.datasource.UserRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.UserRemoteDataSourceImpl
import com.gmail.uli153.ulisestv.data.remote.datasource.VideoRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.VideoRemoteDataSourceImpl
import org.koin.dsl.module

/**
 * `prod` flavor: real remote data sources backed by Retrofit (the APIs come
 * from [dataModule]).
 */
val remoteModule = module {
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<CategoryRemoteDataSource> { CategoryRemoteDataSourceImpl(get()) }
    single<VideoRemoteDataSource> { VideoRemoteDataSourceImpl(get()) }
}
