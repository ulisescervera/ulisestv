package com.gmail.uli153.ulisestv.domain.usecase

import com.gmail.uli153.ulisestv.domain.repository.CategoryRepository

/**
 * Triggers a refresh of the category cache from the remote source.
 */
class RefreshCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke() = categoryRepository.refreshCategories()
}
