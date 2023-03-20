package com.quizmakers.core.data.auth.remote

import com.quizmakers.core.base.Api
import com.quizmakers.core.base.ApiType
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @GET("/authenticate")
    @Api(ApiType.AUTH)
    suspend fun signIn(@Body userAuthenticate: UserAuthenticateRequest): String

    @POST("/register")
    @Api(ApiType.AUTH)
    suspend fun signUp(@Body userRegisterRequest: UserRegisterRequest): String
}