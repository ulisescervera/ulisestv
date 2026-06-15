package com.gmail.uli153.ulisestv.data.remote.datasource

import com.gmail.uli153.ulisestv.data.dto.VideoDto
import com.gmail.uli153.ulisestv.data.remote.api.VideoApi

interface VideoRemoteDataSource {
    suspend fun fetchVideos(): List<VideoDto>
    suspend fun fetchVideosByCategory(categoryId: Int): List<VideoDto>
}

class VideoRemoteDataSourceImpl(
    private val api: VideoApi,
) : VideoRemoteDataSource {

    override suspend fun fetchVideos(): List<VideoDto> = api.getVideos()

    override suspend fun fetchVideosByCategory(categoryId: Int): List<VideoDto> =
        api.getVideosByCategory(categoryId)
}
