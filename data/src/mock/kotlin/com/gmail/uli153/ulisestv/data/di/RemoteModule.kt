package com.gmail.uli153.ulisestv.data.di

import com.gmail.uli153.ulisestv.data.remote.datasource.CategoryRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.MockCategoryRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.MockUserRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.MockVideoRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.UserRemoteDataSource
import com.gmail.uli153.ulisestv.data.remote.datasource.VideoRemoteDataSource
import org.koin.dsl.module

/**
 * `mock` flavor: remote data sources return in-memory mock data and never hit
 * the network. One mock per remote data source.
 */
val remoteModule = module {
    single<UserRemoteDataSource> { MockUserRemoteDataSource() }
    single<CategoryRemoteDataSource> { MockCategoryRemoteDataSource() }
    single<VideoRemoteDataSource> { MockVideoRemoteDataSource() }
}
