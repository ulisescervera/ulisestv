package com.gmail.uli153.ulisestv.core

/**
 * Typed representation of the failure modes of a remote (HTTP) call. Subtypes
 * are extends [Exception] so they propagate through the existing `catch` /
 * [Resource.Error] machinery.
 *
 * 2xx codes (200, 201, …) are successes and never produce a [RemoteError];
 * [forHttpCode] returns `null` for them (see the mapping below).
 */
sealed class RemoteError(
    val code: Int? = null,
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** 400 — malformed request. */
    class BadRequest(message: String? = null) : RemoteError(400, message ?: "Bad request (400)")

    /** 401 — unauthenticated: missing/expired session token (triggers refresh). */
    class Unauthorized(message: String? = null) : RemoteError(401, message ?: "Unauthorized (401)")

    /** 403 — authenticated but not allowed. */
    class Forbidden(message: String? = null) : RemoteError(403, message ?: "Forbidden (403)")

    /** 404 — resource not found. */
    class NotFound(message: String? = null) : RemoteError(404, message ?: "Not found (404)")

    /** 5xx — server-side failure. */
    class ServerError(code: Int = 500, message: String? = null) :
        RemoteError(code, message ?: "Server error ($code)")

    /** Connectivity / timeout failure (no HTTP response). */
    class Network(cause: Throwable? = null) :
        RemoteError(message = "Network unavailable", cause = cause)

    /** Response body could not be parsed. */
    class Serialization(cause: Throwable? = null) :
        RemoteError(message = "Malformed response", cause = cause)

    /** Any other / unknown failure (carries the original code when known). */
    class Unexpected(code: Int? = null, message: String? = null, cause: Throwable? = null) :
        RemoteError(code, message ?: "Unexpected error", cause)

    companion object {
        /**
         * Maps an HTTP status code to a [RemoteError], or `null` for success
         * (2xx, e.g. 200 / 201).
         */
        fun forHttpCode(code: Int, message: String? = null): RemoteError? = when (code) {
            in 200..299 -> null
            400 -> BadRequest(message)
            401 -> Unauthorized(message)
            403 -> Forbidden(message)
            404 -> NotFound(message)
            in 500..599 -> ServerError(code, message)
            else -> Unexpected(code, message)
        }
    }
}
