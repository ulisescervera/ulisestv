package com.gmail.uli153.ulisestv.data.mapper

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.domain.model.Category

fun CategoryDto.toDomain(): Category = Category(
    id = id,
    name = name,
    description = description,
)
