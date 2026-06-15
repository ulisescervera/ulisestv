package com.gmail.uli153.ulisestv.data.error

import com.gmail.uli153.ulisestv.core.RemoteError
import com.gmail.uli153.ulisestv.data.session.SessionManager
import kotlinx.coroutines.CancellationException

/**
 * Executes authenticated remote calls. Failures are mapped to [RemoteError];
 * on a [RemoteError.Unauthorized] (401) — when a refresh token is available —
 * it asks the [SessionManager] to renew the session and retries the call
 * exactly once.
 *
 * The refresh call itself is NOT routed through this handler, so a 401 from the
 * refresh endpoint cannot cause an infinite loop. The session token is owned by
 * the [SessionManager]; it is never seen here or by the domain layer.
 */
class RemoteCallHandler(
    private val sessionManager: SessionManager,
) {
    suspend fun <T> execute(block: suspend () -> T): T {
        return try {
            block()
        } catch (throwable: Throwable) {
            // toRemoteError() rethrows CancellationException untouched.
            val error = throwable.toRemoteError()
            if (error is RemoteError.Unauthorized && sessionManager.hasRefreshToken()) {
                refreshAndRetry(block, original = error)
            } else {
                throw error
            }
        }
    }

    private suspend fun <T> refreshAndRetry(block: suspend () -> T, original: RemoteError): T {
        try {
            sessionManager.refreshSession()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (refreshFailure: Throwable) {
            // Refresh failed (e.g. refresh token also expired): the session is
            // unrecoverable, so clear it and surface the original 401.
            sessionManager.clear()
            throw original
        }
        return try {
            block()
        } catch (retryFailure: Throwable) {
            throw retryFailure.toRemoteError()
        }
    }
}
