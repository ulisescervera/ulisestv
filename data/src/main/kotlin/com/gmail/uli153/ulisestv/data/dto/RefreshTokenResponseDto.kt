package com.gmail.uli153.ulisestv.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenResponseDto(
    @Json(name = "token") val token: String,
    @Json(name = "refreshToken") val refreshToken: String,
)
