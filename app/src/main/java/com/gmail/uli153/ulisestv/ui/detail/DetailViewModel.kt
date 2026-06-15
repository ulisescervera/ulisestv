package com.gmail.uli153.ulisestv.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.domain.usecase.SearchVideoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(
    private val searchVideoUseCase: SearchVideoUseCase,
) : ViewModel() {

    private val videoId = MutableStateFlow<Int?>(null)

    /** The video matching the requested id, resolved from the cached catalog. */
    val video: StateFlow<Video?> = videoId
        .flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                searchVideoUseCase("").map { resource ->
                    (resource as? Resource.Success)?.data?.firstOrNull { it.id == id }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setVideoId(id: Int) {
        videoId.value = id
    }
}
