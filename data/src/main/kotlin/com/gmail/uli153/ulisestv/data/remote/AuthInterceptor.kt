package com.gmail.uli153.ulisestv.data.remote

import com.gmail.uli153.ulisestv.data.local.security.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Attaches the stored session token as a `Authorization: Bearer <token>` header
 * to every outgoing request (when a token is available).
 */
class AuthInterceptor(
    private val tokenStorage: TokenStorage,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenStorage.token()
        // The refresh endpoint authenticates with the refresh token in the body,
        // so it must not carry the (likely expired) session bearer.
        val isRefresh = original.url.encodedPath.endsWith(REFRESH_PATH)
        val request = if (token.isNullOrBlank() || isRefresh) {
            original
        } else {
            original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(request)
    }

    private companion object {
        const val REFRESH_PATH = "auth/refresh"
    }
}
