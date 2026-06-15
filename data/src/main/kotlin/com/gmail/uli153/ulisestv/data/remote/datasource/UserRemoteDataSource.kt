package com.gmail.uli153.ulisestv.data.remote.datasource

import com.gmail.uli153.ulisestv.data.dto.LoginRequestDto
import com.gmail.uli153.ulisestv.data.dto.RefreshTokenRequestDto
import com.gmail.uli153.ulisestv.data.dto.RefreshTokenResponseDto
import com.gmail.uli153.ulisestv.data.dto.UserDto
import com.gmail.uli153.ulisestv.data.remote.api.UserApi

interface UserRemoteDataSource {
    suspend fun login(email: String, password: String): UserDto

    /** Invalidates the current session token on the backend. */
    suspend fun logout()

    /** Exchanges a refresh token for a fresh session token. */
    suspend fun refreshToken(refreshToken: String): RefreshTokenResponseDto
}

class UserRemoteDataSourceImpl(
    private val api: UserApi,
) : UserRemoteDataSource {

    override suspend fun login(email: String, password: String): UserDto =
        api.login(LoginRequestDto(email = email, password = password))

    override suspend fun logout() = api.logout()

    override suspend fun refreshToken(refreshToken: String): RefreshTokenResponseDto =
        api.refresh(RefreshTokenRequestDto(refreshToken))
}
