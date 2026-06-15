package com.gmail.uli153.ulisestv.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "surname") val surname: String,
    @Json(name = "email") val email: String,
    // Auth tokens: kept in the DTO but intentionally NOT mapped to the domain
    // User. They are persisted in secure storage by the repository.
    @Json(name = "token") val token: String,
    @Json(name = "refreshToken") val refreshToken: String,
) {
    companion object
}
