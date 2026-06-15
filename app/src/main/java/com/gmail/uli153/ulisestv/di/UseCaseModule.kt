package com.gmail.uli153.ulisestv.di

import com.gmail.uli153.ulisestv.domain.usecase.LoginUseCase
import com.gmail.uli153.ulisestv.domain.usecase.LogoutUseCase
import com.gmail.uli153.ulisestv.domain.usecase.RefreshCategoriesUseCase
import com.gmail.uli153.ulisestv.domain.usecase.RefreshVideosUseCase
import com.gmail.uli153.ulisestv.domain.usecase.SearchCategoryUseCase
import com.gmail.uli153.ulisestv.domain.usecase.SearchVideoUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { SearchCategoryUseCase(get()) }
    factory { SearchVideoUseCase(get()) }
    factory { RefreshCategoriesUseCase(get()) }
    factory { RefreshVideosUseCase(get()) }
}
