package com.gmail.uli153.ulisestv.domain.repository

import com.gmail.uli153.ulisestv.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /** Authenticates the user and returns the resulting [User]. */
    suspend fun login(email: String, password: String): User

    /** Clears the current session. */
    suspend fun logout()

    /** Emits the currently logged-in user, or null when there is no session. */
    fun currentUser(): Flow<User?>
}
