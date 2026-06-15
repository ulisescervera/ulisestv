package com.gmail.uli153.ulisestv.data.local.datasource

import com.gmail.uli153.ulisestv.data.dto.VideoDto
import com.gmail.uli153.ulisestv.data.local.dao.VideoDao
import kotlinx.coroutines.flow.Flow

interface VideoLocalDataSource {
    fun getVideos(): Flow<List<VideoDto>>
    fun getVideosByCategory(categoryId: Int): Flow<List<VideoDto>>
    fun searchVideos(query: String): Flow<List<VideoDto>>

    /** Replaces the whole videos table (full refresh). */
    suspend fun saveVideos(videos: List<VideoDto>)

    /** Inserts/updates the given videos without clearing the table (partial refresh). */
    suspend fun upsertVideos(videos: List<VideoDto>)
}

class VideoLocalDataSourceImpl(
    private val dao: VideoDao,
) : VideoLocalDataSource {

    override fun getVideos(): Flow<List<VideoDto>> = dao.getAll()

    override fun getVideosByCategory(categoryId: Int): Flow<List<VideoDto>> =
        dao.getByCategory(categoryId)

    override fun searchVideos(query: String): Flow<List<VideoDto>> = dao.search(query)

    override suspend fun saveVideos(videos: List<VideoDto>) = dao.replaceAll(videos)

    override suspend fun upsertVideos(videos: List<VideoDto>) = dao.upsertAll(videos)
}
