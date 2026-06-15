package com.gmail.uli153.ulisestv.data.session

import com.gmail.uli153.ulisestv.core.DispatcherProvider
import com.gmail.uli153.ulisestv.core.RemoteError
import com.gmail.uli153.ulisestv.data.error.safeRemoteCall
import com.gmail.uli153.ulisestv.data.local.security.TokenStorage
import com.gmail.uli153.ulisestv.data.remote.datasource.UserRemoteDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Owns the auth session. Tokens live entirely in the data layer here and are
 * never exposed to the domain layer.
 */
interface SessionManager {
    /** Current session token for outbound requests, or null when signed out. */
    fun sessionToken(): String?

    /** True when a refresh token is available to renew the session. */
    fun hasRefreshToken(): Boolean

    /** Persists the tokens issued by a successful login. */
    fun saveSession(sessionToken: String, refreshToken: String)

    /** Renews and persists the session token using the stored refresh token. */
    suspend fun refreshSession()

    /** Drops the stored session. */
    fun clear()
}

class SessionManagerImpl(
    private val remote: UserRemoteDataSource,
    private val tokenStorage: TokenStorage,
    private val dispatchers: DispatcherProvider,
) : SessionManager {

    // Serializes concurrent refreshes (several parallel 401s -> one refresh).
    private val refreshMutex = Mutex()

    override fun sessionToken(): String? = tokenStorage.token()

    override fun hasRefreshToken(): Boolean = tokenStorage.refreshToken() != null

    override fun saveSession(sessionToken: String, refreshToken: String) =
        tokenStorage.saveTokens(sessionToken, refreshToken)

    override suspend fun refreshSession() = withContext(dispatchers.io) {
        val tokenBeforeLock = tokenStorage.token()
        refreshMutex.withLock {
            // If another caller refreshed while we waited for the lock, we're done.
            val current = tokenStorage.token()
            if (current != null && current != tokenBeforeLock) {
                return@withLock
            }
            val refreshToken = tokenStorage.refreshToken()
                ?: throw RemoteError.Unauthorized("No refresh token available")
            val response = safeRemoteCall { remote.refreshToken(refreshToken) }
            // The new session token is stored here (and read back by the
            // AuthInterceptor) — it is never returned to the caller / domain.
            tokenStorage.saveTokens(response.token, response.refreshToken)
        }
    }

    override fun clear() = tokenStorage.clear()
}
