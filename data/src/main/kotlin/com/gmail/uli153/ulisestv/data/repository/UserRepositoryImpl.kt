package com.gmail.uli153.ulisestv.data.repository

import com.gmail.uli153.ulisestv.core.DispatcherProvider
import com.gmail.uli153.ulisestv.data.error.safeRemoteCall
import com.gmail.uli153.ulisestv.data.mapper.toDomain
import com.gmail.uli153.ulisestv.data.remote.datasource.UserRemoteDataSource
import com.gmail.uli153.ulisestv.data.session.SessionManager
import com.gmail.uli153.ulisestv.domain.model.User
import com.gmail.uli153.ulisestv.domain.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val remote: UserRemoteDataSource,
    private val sessionManager: SessionManager,
    private val dispatchers: DispatcherProvider,
) : UserRepository {

    private val currentUser = MutableStateFlow<User?>(null)

    override suspend fun login(email: String, password: String): User =
        withContext(dispatchers.io) {
            val dto = safeRemoteCall { remote.login(email, password) }
            // token & refreshToken are NOT part of the domain model: hand them to
            // the SessionManager, which owns and persists them.
            sessionManager.saveSession(dto.token, dto.refreshToken)
            val user = dto.toDomain()
            currentUser.value = user
            user
        }

    override suspend fun logout() = withContext(dispatchers.io) {
        try {
            // Best-effort: invalidate server-side; AuthInterceptor still carries the token.
            remote.logout()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: Throwable) {
            // Ignore remote failures — we always clear the local session below.
        } finally {
            sessionManager.clear()
            currentUser.value = null
        }
    }

    override fun currentUser(): Flow<User?> = currentUser.asStateFlow()
}
