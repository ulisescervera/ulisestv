package com.gmail.uli153.ulisestv.core

/**
 * Generic wrapper used across layers to model the state of an asynchronous
 * operation (loading / success / error). Lives in [core] so every module can
 * reuse it without coupling to a specific layer.
 */
sealed interface Resource<out T> {

    data object Loading : Resource<Nothing>

    data class Success<out T>(val data: T) : Resource<T>

    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : Resource<Nothing>
}

inline fun <T> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

inline fun <T> Resource<T>.onError(action: (String, Throwable?) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message, throwable)
    return this
}
