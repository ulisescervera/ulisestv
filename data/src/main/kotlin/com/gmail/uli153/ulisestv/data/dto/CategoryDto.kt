package com.gmail.uli153.ulisestv.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Doubles as a Moshi DTO (remote JSON) and a Room entity (local cache).
 */
@Entity(tableName = "categories")
@JsonClass(generateAdapter = true)
data class CategoryDto(
    @PrimaryKey
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
) {
    companion object
}
