package com.gmail.uli153.ulisestv.ui.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.domain.model.Video
import com.gmail.uli153.ulisestv.domain.usecase.RefreshVideosUseCase
import com.gmail.uli153.ulisestv.domain.usecase.SearchVideoUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class VideosUiState(
    val isLoading: Boolean = false,
    val videos: List<Video> = emptyList(),
    val error: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
class VideosViewModel(
    private val searchVideoUseCase: SearchVideoUseCase,
    private val refreshVideosUseCase: RefreshVideosUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val categoryId = MutableStateFlow(NO_CATEGORY)

    private val _uiState = MutableStateFlow(VideosUiState())
    val uiState: StateFlow<VideosUiState> = _uiState.asStateFlow()

    init {
        combine(query, categoryId) { q, cat -> q to cat }
            .flatMapLatest { (q, cat) ->
                searchVideoUseCase(q).map { resource -> resource to cat }
            }
            .onEach { (resource, cat) ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Resource.Success -> {
                        val visible = if (cat == NO_CATEGORY) {
                            resource.data
                        } else {
                            resource.data.filter { it.category.id == cat }
                        }
                        _uiState.value.copy(isLoading = false, videos = visible, error = null)
                    }
                    is Resource.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = resource.message,
                    )
                }
            }
            .launchIn(viewModelScope)

        refresh()
    }

    fun setCategory(id: Int) {
        if (categoryId.value == id) return
        categoryId.value = id
        refresh()
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val cat = categoryId.value
                refreshVideosUseCase(if (cat == NO_CATEGORY) null else cat)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Refresh failed")
            }
        }
    }

    companion object {
        const val NO_CATEGORY = -1
    }
}
