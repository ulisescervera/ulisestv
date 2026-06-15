package com.gmail.uli153.ulisestv.data.remote.api

import com.gmail.uli153.ulisestv.data.dto.LoginRequestDto
import com.gmail.uli153.ulisestv.data.dto.RefreshTokenRequestDto
import com.gmail.uli153.ulisestv.data.dto.RefreshTokenResponseDto
import com.gmail.uli153.ulisestv.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): UserDto

    /** Invalidates the current session token server-side (token sent by AuthInterceptor). */
    @POST("auth/logout")
    suspend fun logout()

    /** Exchanges a refresh token for a new session token. */
    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequestDto): RefreshTokenResponseDto
}
