package com.gmail.uli153.ulisestv.domain.repository

import com.gmail.uli153.ulisestv.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    /** Stream of all cached categories. */
    fun getCategories(): Flow<List<Category>>

    /** Stream of categories whose name matches [query]. */
    fun searchCategories(query: String): Flow<List<Category>>

    /** Forces a refresh from the remote source into the local cache. */
    suspend fun refreshCategories()
}
