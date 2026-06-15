package com.gmail.uli153.ulisestv.data.repository

import com.gmail.uli153.ulisestv.core.DispatcherProvider
import com.gmail.uli153.ulisestv.data.error.RemoteCallHandler
import com.gmail.uli153.ulisestv.data.local.datasource.VideoLocalDataSource
import com.gmail.uli153.ulisestv.data.mapper.toDomain
import com.gmail.uli153.ulisestv.data.remote.datasource.VideoRemoteDataSource
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VideoRepositoryImpl(
    private val remote: VideoRemoteDataSource,
    private val local: VideoLocalDataSource,
    private val dispatchers: DispatcherProvider,
    private val callHandler: RemoteCallHandler,
) : VideoRepository {

    override fun getVideos(): Flow<List<Video>> =
        local.getVideos()
            .map { dtos -> dtos.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override fun getVideosByCategory(categoryId: Int): Flow<List<Video>> =
        local.getVideosByCategory(categoryId)
            .map { dtos -> dtos.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override fun searchVideos(query: String): Flow<List<Video>> =
        local.searchVideos(query)
            .map { dtos -> dtos.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override suspend fun refreshVideos() = withContext(dispatchers.io) {
        val remoteVideos = callHandler.execute { remote.fetchVideos() }
        local.saveVideos(remoteVideos)
    }

    override suspend fun refreshVideosByCategory(categoryId: Int) = withContext(dispatchers.io) {
        val remoteVideos = callHandler.execute { remote.fetchVideosByCategory(categoryId) }
        // Partial refresh: merge this category's videos without wiping the rest.
        local.upsertVideos(remoteVideos)
    }
}
