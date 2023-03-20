package com.quizmakers.core.data.quizzes.remote

import com.quizmakers.core.base.Api
import com.quizmakers.core.base.ApiType
import retrofit2.http.Body
import retrofit2.http.GET

//TODO After api will by ready replace body or query with correct data.
interface QuizzesService {
    @GET("/quizzes")
    @Api(ApiType.QUIZZES)
    suspend fun getQuizzes(@Body params: String): String
}