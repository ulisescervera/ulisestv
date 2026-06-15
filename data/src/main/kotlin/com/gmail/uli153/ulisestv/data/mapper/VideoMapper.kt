package com.gmail.uli153.ulisestv.data.mapper

import com.gmail.uli153.ulisestv.data.dto.VideoDto
import com.gmail.uli153.ulisestv.domain.model.Video

fun VideoDto.toDomain(): Video = Video(
    id = id,
    name = name,
    url = url,
    category = category.toDomain(),
    description = description,
)
