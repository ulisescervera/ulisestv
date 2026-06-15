package com.gmail.uli153.ulisestv.data.repository

import com.gmail.uli153.ulisestv.core.DispatcherProvider
import com.gmail.uli153.ulisestv.data.error.RemoteCallHandler
import com.gmail.uli153.ulisestv.data.local.datasource.CategoryLocalDataSource
import com.gmail.uli153.ulisestv.data.mapper.toDomain
import com.gmail.uli153.ulisestv.data.remote.datasource.CategoryRemoteDataSource
import com.gmail.uli153.ulisestv.domain.model.Category
import com.gmail.uli153.ulisestv.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val remote: CategoryRemoteDataSource,
    private val local: CategoryLocalDataSource,
    private val dispatchers: DispatcherProvider,
    private val callHandler: RemoteCallHandler,
) : CategoryRepository {

    // Cache is the single source of truth; mapping DTO -> domain happens here.
    override fun getCategories(): Flow<List<Category>> =
        local.getCategories()
            .map { dtos -> dtos.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override fun searchCategories(query: String): Flow<List<Category>> =
        local.searchCategories(query)
            .map { dtos -> dtos.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override suspend fun refreshCategories() = withContext(dispatchers.io) {
        // Maps failures to RemoteError and refreshes the session on a 401.
        val remoteCategories = callHandler.execute { remote.fetchCategories() }
        local.saveCategories(remoteCategories)
    }
}
