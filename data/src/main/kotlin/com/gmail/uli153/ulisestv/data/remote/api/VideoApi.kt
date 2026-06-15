package com.gmail.uli153.ulisestv.data.remote.api

import com.gmail.uli153.ulisestv.data.dto.VideoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApi {

    @GET("videos")
    suspend fun getVideos(): List<VideoDto>

    @GET("videos")
    suspend fun getVideosByCategory(@Query("categoryId") categoryId: Int): List<VideoDto>
}
