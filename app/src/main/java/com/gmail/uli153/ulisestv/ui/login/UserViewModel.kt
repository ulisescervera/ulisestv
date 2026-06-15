package com.gmail.uli153.ulisestv.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.domain.model.User
import com.gmail.uli153.ulisestv.domain.usecase.LoginUseCase
import com.gmail.uli153.ulisestv.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class UserUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
) {
    val isLoggedIn: Boolean get() = user != null
}

class UserViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        loginUseCase(email, password)
            .onEach { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Resource.Success -> _uiState.value.copy(
                        isLoading = false,
                        user = resource.data,
                        error = null,
                    )
                    is Resource.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = resource.message,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.value = UserUiState()
        }
    }
}
