package com.quizmakers.quizup.core.api

import retrofit2.http.GET

//TODO After api will by ready replace body or query with correct data.
interface QuizUpApiService {

    @GET("/login")
    suspend fun loginUser(): String

    @GET("/register")
    suspend fun registerUser(): String
}