package com.gmail.uli153.ulisestv.data.remote.datasource

import com.gmail.uli153.ulisestv.data.dto.CategoryDto
import com.gmail.uli153.ulisestv.data.dto.RefreshTokenResponseDto
import com.gmail.uli153.ulisestv.data.dto.UserDto
import com.gmail.uli153.ulisestv.data.dto.VideoDto
import com.gmail.uli153.ulisestv.data.mock.mock
import com.gmail.uli153.ulisestv.data.mock.mocks
import kotlinx.coroutines.delay

private const val MOCK_DELAY_MS = 400L

/** `mock` flavor implementation of [UserRemoteDataSource]. */
class MockUserRemoteDataSource : UserRemoteDataSource {

    override suspend fun login(email: String, password: String): UserDto {
        delay(MOCK_DELAY_MS)
        // Echo the requested email so the mock session looks consistent.
        return UserDto.mock().copy(email = email)
    }

    override suspend fun logout() {
        delay(MOCK_DELAY_MS)
    }

    override suspend fun refreshToken(refreshToken: String): RefreshTokenResponseDto {
        delay(MOCK_DELAY_MS)
        return RefreshTokenResponseDto(
            token = "mock-session-token-refreshed",
            refreshToken = refreshToken,
        )
    }
}

/** `mock` flavor implementation of [CategoryRemoteDataSource]. */
class MockCategoryRemoteDataSource : CategoryRemoteDataSource {

    override suspend fun fetchCategories(): List<CategoryDto> {
        delay(MOCK_DELAY_MS)
        return CategoryDto.mocks()
    }
}

/** `mock` flavor implementation of [VideoRemoteDataSource]. */
class MockVideoRemoteDataSource : VideoRemoteDataSource {

    override suspend fun fetchVideos(): List<VideoDto> {
        delay(MOCK_DELAY_MS)
        return VideoDto.mocks()
    }

    override suspend fun fetchVideosByCategory(categoryId: Int): List<VideoDto> {
        delay(MOCK_DELAY_MS)
        return VideoDto.mocks().filter { it.category.id == categoryId }
    }
}
