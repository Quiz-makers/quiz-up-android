package com.quizmakers.quizup.core.api

import com.quizmakers.quizup.core.data.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET

//TODO After api will by ready replace body or query with correct data.
interface QuizUpApiService {

    @GET("/signIn")
    suspend fun signIn(@Body params: String): String

    @GET("/signUp")
    suspend fun signUp(@Body user: UserRequest): String

}