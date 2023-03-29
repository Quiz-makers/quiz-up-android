package com.quizmakers.core.data.quizzes.remote

import retrofit2.http.Body
import retrofit2.http.GET

//TODO After api will by ready replace body or query with correct data.
interface QuizzesService {
    //TODO IF HADER WITH INTERCEPTOR DONT WORK ADD @HEADER
    @GET("/quizzes")
    suspend fun getQuizzes(): List<String>
}