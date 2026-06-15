package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.domain.model.User
import com.gmail.uli153.ulisestv.domain.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Authenticates a user, exposing the progress as a [Resource] stream.
 */
class LoginUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val user = userRepository.login(email, password)
            emit(Resource.Success(user))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed", e))
        }
    }
}
