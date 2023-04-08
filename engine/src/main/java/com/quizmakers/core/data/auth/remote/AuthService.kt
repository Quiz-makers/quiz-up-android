package com.quizmakers.core.data.auth.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/authenticate")
    suspend fun signIn(@Body userAuthenticate: UserAuthenticateRequest): Response<Unit>

    @POST("/auth/register")
    suspend fun signUp(@Body userRegisterRequest: UserRegisterRequest): Response<Unit>
}