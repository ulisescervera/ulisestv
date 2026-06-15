package com.gmail.uli153.ulisestv.data.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Doubles as a Moshi DTO (remote JSON) and a Room entity (local cache).
 *
 * The category travels nested inside the video: Moshi (de)serializes it as a
 * nested JSON object, while Room flattens it into prefixed columns
 * (category_id, category_name, category_description) via [Embedded].
 */
@Entity(tableName = "videos")
@JsonClass(generateAdapter = true)
data class VideoDto(
    @PrimaryKey
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String,
    @Embedded(prefix = "category_")
    @Json(name = "category") val category: CategoryDto,
    @Json(name = "description") val description: String,
) {
    companion object
}
