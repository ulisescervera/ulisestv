package com.gmail.uli153.ulisestv.data.mapper

import com.gmail.uli153.ulisestv.data.dto.UserDto
import com.gmail.uli153.ulisestv.domain.model.User

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    surname = surname,
    email = email,
)
