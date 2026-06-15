package com.gmail.uli153.ulisestv.data.local.datasource

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.data.local.dao.CategoryDao
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {
    fun getCategories(): Flow<List<CategoryDto>>
    fun searchCategories(query: String): Flow<List<CategoryDto>>
    suspend fun saveCategories(categories: List<CategoryDto>)
}

class CategoryLocalDataSourceImpl(
    private val dao: CategoryDao,
) : CategoryLocalDataSource {

    override fun getCategories(): Flow<List<CategoryDto>> = dao.getAll()

    override fun searchCategories(query: String): Flow<List<CategoryDto>> = dao.search(query)

    override suspend fun saveCategories(categories: List<CategoryDto>) = dao.replaceAll(categories)
}
