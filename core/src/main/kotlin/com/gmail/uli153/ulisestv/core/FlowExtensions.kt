package com.gmail.uli153.ulisestv.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Wraps the emissions of a [Flow] into [Resource], emitting [Resource.Loading]
 * first and converting any thrown exception into [Resource.Error].
 */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> =
    map<T, Resource<T>> { Resource.Success(it) }
        .onStart { emit(Resource.Loading) }
        .catch { emit(Resource.Error(it.message ?: "Unexpected error", it)) }
