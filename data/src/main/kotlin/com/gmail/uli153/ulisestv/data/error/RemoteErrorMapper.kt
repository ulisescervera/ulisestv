package com.gmail.uli153.ulisestv.data.error

import com.gmail.uli153.ulisestv.core.RemoteError
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Maps a low-level throwable from the network/serialization stack into a typed
 * [RemoteError]. [CancellationException] is rethrown untouched so coroutine
 * cancellation keeps propagating (structured concurrency).
 */
fun Throwable.toRemoteError(): RemoteError = when (this) {
    is CancellationException -> throw this
    is RemoteError -> this
    // HttpException.message() is the status line, not the body: rely on the
    // curated per-code default messages instead of clobbering them.
    is HttpException -> RemoteError.forHttpCode(code()) ?: RemoteError.Unexpected(code())
    is JsonDataException, is JsonEncodingException -> RemoteError.Serialization(this)
    is IOException -> RemoteError.Network(this) // timeouts, no connectivity, etc.
    else -> RemoteError.Unexpected(message = message, cause = this)
}

/**
 * Runs [block], converting any failure into a [RemoteError]. No retry / refresh
 * logic — use [RemoteCallHandler] for authenticated calls that should refresh
 * the session on a 401.
 */
suspend fun <T> safeRemoteCall(block: suspend () -> T): T =
    try {
        block()
    } catch (throwable: Throwable) {
        throw throwable.toRemoteError()
    }
