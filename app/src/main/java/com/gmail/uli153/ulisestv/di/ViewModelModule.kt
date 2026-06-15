package com.gmail.uli153.ulisestv.di

import com.gmail.uli153.ulisestv.ui.categories.CategoriesViewModel
import com.gmail.uli153.ulisestv.ui.detail.DetailViewModel
import com.gmail.uli153.ulisestv.ui.login.UserViewModel
import com.gmail.uli153.ulisestv.ui.videos.VideosViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::UserViewModel)
    viewModelOf(::CategoriesViewModel)
    viewModelOf(::VideosViewModel)
    viewModelOf(::DetailViewModel)
}
