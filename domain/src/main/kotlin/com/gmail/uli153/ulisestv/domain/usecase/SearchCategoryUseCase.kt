package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.core.Resource
import com.gmail.uli153.ulisestv.core.asResource
import com.gmail.uli153.ulisestv.domain.model.Category
import com.gmail.uli153.ulisestv.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns categories matching [query]. A blank query returns all categories.
 */
class SearchCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(query: String): Flow<Resource<List<Category>>> {
        val source = if (query.isBlank()) {
            categoryRepository.getCategories()
        } else {
            categoryRepository.searchCategories(query)
        }
        return source.asResource()
    }
}
