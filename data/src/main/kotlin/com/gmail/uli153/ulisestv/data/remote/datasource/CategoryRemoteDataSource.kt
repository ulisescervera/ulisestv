package com.gmail.uli153.ulisestv.data.remote.datasource

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.data.remote.api.CategoryApi

interface CategoryRemoteDataSource {
    suspend fun fetchCategories(): List<CategoryDto>
}

class CategoryRemoteDataSourceImpl(
    private val api: CategoryApi,
) : CategoryRemoteDataSource {

    override suspend fun fetchCategories(): List<CategoryDto> = api.getCategories()
}
