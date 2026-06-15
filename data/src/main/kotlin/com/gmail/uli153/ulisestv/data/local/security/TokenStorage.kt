package com.gmail.uli153.ulisestv.data.local.security

import android.content.Context
import android.content.SharedPreferences

/**
 * Secure storage for the session tokens. Tokens never reach the domain layer.
 */
interface TokenStorage {
    fun saveTokens(token: String, refreshToken: String)
    fun token(): String?
    fun refreshToken(): String?
    fun clear()
}

/**
 * [TokenStorage] that encrypts each value with an AES-256-GCM key from the
 * Android Keystore (see [KeystoreCryptoManager]) and stores the resulting
 * ciphertext in regular [SharedPreferences]. This is the recommended
 * replacement for the deprecated `EncryptedSharedPreferences`, while still
 * keeping the encryption key inside the Android "secure keys".
 *
 * The decrypted tokens are cached in memory so the (relatively expensive)
 * Keystore decryption runs at most once per process — on the first read after
 * a cold start, or not at all when the session is established via [saveTokens].
 * Every subsequent read (e.g. the per-request `AuthInterceptor`) is just a
 * volatile field read. The token is necessarily in cleartext in memory while a
 * request uses it anyway, so caching it does not weaken security; the value at
 * rest in [SharedPreferences] stays encrypted.
 */
class SecureTokenStorage(
    context: Context,
    private val crypto: KeystoreCryptoManager = KeystoreCryptoManager(),
) : TokenStorage {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @Volatile private var cachedToken: String? = null
    @Volatile private var cachedRefreshToken: String? = null

    /** Whether the cache has been populated from disk this process. */
    @Volatile private var loaded = false

    /** Decrypts from prefs into the in-memory cache once (double-checked). */
    private fun ensureLoaded() {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            cachedToken = prefs.getString(KEY_TOKEN, null)?.let { crypto.decrypt(it) }
            cachedRefreshToken = prefs.getString(KEY_REFRESH_TOKEN, null)?.let { crypto.decrypt(it) }
            loaded = true
        }
    }

    override fun saveTokens(token: String, refreshToken: String) {
        // Update the in-memory cache first so no caller has to hit the Keystore.
        cachedToken = token
        cachedRefreshToken = refreshToken
        loaded = true
        prefs.edit()
            .putString(KEY_TOKEN, crypto.encrypt(token))
            .putString(KEY_REFRESH_TOKEN, crypto.encrypt(refreshToken))
            .apply()
    }

    override fun token(): String? {
        ensureLoaded()
        return cachedToken
    }

    override fun refreshToken(): String? {
        ensureLoaded()
        return cachedRefreshToken
    }

    override fun clear() {
        cachedToken = null
        cachedRefreshToken = null
        loaded = true // a cleared session is a known (empty) state, no need to re-read disk
        prefs.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "ulisestv_secure_tokens"
        const val KEY_TOKEN = "session_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
