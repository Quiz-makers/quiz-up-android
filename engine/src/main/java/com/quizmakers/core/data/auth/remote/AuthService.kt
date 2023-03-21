package com.quizmakers.core.data.auth.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @GET("auth/authenticate")
    suspend fun signIn(@Body userAuthenticate: UserAuthenticateRequest): String

    @POST("auth/register")
    suspend fun signUp(@Body userRegisterRequest: UserRegisterRequest): String
}