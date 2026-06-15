package com.gmail.uli153.ulisestv.data.mock

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.data.dto.UserDto
import com.gmail.uli153.ulisestv.data.dto.VideoDto

/**
 * Mock factories for the DTOs, only available in the `mock` flavor.
 * They are companion-object extensions so they read as static factories:
 * `UserDto.mock()`, `CategoryDto.mocks()`, `VideoDto.mocks()`.
 */

fun UserDto.Companion.mock(): UserDto = UserDto(
    id = 1,
    name = "Mock",
    surname = "User",
    email = "mock.user@ulisestv.com",
    token = "mock-session-token",
    refreshToken = "mock-refresh-token",
)

fun CategoryDto.Companion.mocks(): List<CategoryDto> = listOf(
    CategoryDto(id = 1, name = "Movies", description = "Feature films"),
    CategoryDto(id = 2, name = "Series", description = "TV series"),
    CategoryDto(id = 3, name = "Documentaries", description = "Documentary films"),
    CategoryDto(id = 4, name = "Kids", description = "Content for children"),
)

fun VideoDto.Companion.mocks(): List<VideoDto> {
    val categories = CategoryDto.mocks()
    val movies = categories[0]
    val series = categories[1]
    val documentaries = categories[2]
    return listOf(
        VideoDto(
            id = 1,
            name = "Big Buck Bunny",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = movies,
            description = "A large rabbit deals with three bullies.",
        ),
        VideoDto(
            id = 2,
            name = "Elephants Dream",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            category = movies,
            description = "The first Blender Open Movie from 2006.",
        ),
        VideoDto(
            id = 3,
            name = "For Bigger Blazes",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            category = series,
            description = "Introducing Chromecast.",
        ),
        VideoDto(
            id = 4,
            name = "Sintel",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            category = documentaries,
            description = "Sintel is an independently produced short film.",
        ),
        VideoDto(
            id = 5,
            name = "Tears of Steel",
            url = "https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            category = documentaries,
            description = "Live-action and CGI Blender Open Movie.",
        ),
    )
}
