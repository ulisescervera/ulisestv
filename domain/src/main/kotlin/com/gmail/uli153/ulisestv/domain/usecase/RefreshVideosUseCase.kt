package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.domain.repository.VideoRepository

/**
 * Triggers a refresh of the video cache. When [categoryId] is provided the
 * refresh is scoped to that category (server-side); otherwise all videos are
 * refreshed.
 */
class RefreshVideosUseCase(
    private val videoRepository: VideoRepository,
) {
    suspend operator fun invoke(categoryId: Int? = null) {
        if (categoryId == null) {
            videoRepository.refreshVideos()
        } else {
            videoRepository.refreshVideosByCategory(categoryId)
        }
    }
}
