package com.quizmakers.core.data.quizzes.remote

import retrofit2.http.Body
import retrofit2.http.GET

//TODO After api will by ready replace body or query with correct data.
interface QuizzesService {
    @GET("/quizzes")
    suspend fun getQuizzes(@Body params: String): String
}