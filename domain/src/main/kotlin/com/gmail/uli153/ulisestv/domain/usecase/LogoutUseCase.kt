package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.domain.repository.UserRepository

/**
 * Clears the current user session.
 */
class LogoutUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() = userRepository.logout()
}
