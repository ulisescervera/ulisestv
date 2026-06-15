package com.gmail.uli153.ulisestv.domain.model

data class Video(
    val id: Int,
    val name: String,
    val url: String,
    val category: Category,
    val description: String,
)
