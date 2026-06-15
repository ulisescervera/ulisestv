package com.gmail.uli153.ulisestv.data.remote.api

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import retrofit2.http.GET

interface CategoryApi {

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}
