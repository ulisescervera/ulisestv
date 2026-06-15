package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.core.asResource
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns videos matching [query]. A blank query returns all videos.
 */
class SearchVideoUseCase(
    private val videoRepository: VideoRepository,
) {
    operator fun invoke(query: String): Flow<Resource<List<Video>>> {
        val source = if (query.isBlank()) {
            videoRepository.getVideos()
        } else {
            videoRepository.searchVideos(query)
        }
        return source.asResource()
    }
}
