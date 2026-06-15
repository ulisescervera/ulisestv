package com.gmail.uli153.ulisestv.domain.repository

import com.gmail.uli153.ulisestv.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    /** Stream of all cached videos. */
    fun getVideos(): Flow<List<Video>>

    /** Stream of videos belonging to the given category. */
    fun getVideosByCategory(categoryId: Int): Flow<List<Video>>

    /** Stream of videos whose name matches [query]. */
    fun searchVideos(query: String): Flow<List<Video>>

    /** Forces a full refresh of all videos from the remote source into the local cache. */
    suspend fun refreshVideos()

    /** Refreshes only the videos of the given category from the remote source. */
    suspend fun refreshVideosByCategory(categoryId: Int)
}
