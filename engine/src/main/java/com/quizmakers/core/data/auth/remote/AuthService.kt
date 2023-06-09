package com.quizmakers.core.data.auth.remote

import com.quizmakers.core.data.quizzes.remote.AuthResponseApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/authenticate")
    @Headers("No-Authentication: true")
    suspend fun signIn(@Body userAuthenticate: UserAuthenticateRequest): Response<AuthResponseApi>

    @POST("/auth/register")
    @Headers("No-Authentication: true")
    suspend fun register(@Body userRegisterRequest: UserRegisterRequest): Response<Unit>
}