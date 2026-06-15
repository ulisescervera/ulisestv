package com.gmail.uli153.ulisestv.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.domain.model.Category
import com.gmail.uli153.ulisestv.domain.usecase.RefreshCategoriesUseCase
import com.gmail.uli153.ulisestv.domain.usecase.SearchCategoryUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModel(
    private val searchCategoryUseCase: SearchCategoryUseCase,
    private val refreshCategoriesUseCase: RefreshCategoriesUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        query
            .flatMapLatest { value -> searchCategoryUseCase(value) }
            .onEach { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Resource.Success -> _uiState.value.copy(
                        isLoading = false,
                        categories = resource.data,
                        error = null,
                    )
                    is Resource.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = resource.message,
                    )
                }
            }
            .launchIn(viewModelScope)

        refresh()
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                refreshCategoriesUseCase()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Refresh failed")
            }
        }
    }
}
