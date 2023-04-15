package com.quizmakers.core.data.quizzes.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//TODO After api will by ready replace body or query with correct data.
interface QuizzesService {
    @GET("/quizapp/quiz/quizzes")
    suspend fun getPublicQuizzes(): List<QuizResponseApi>

    @GET("/quizapp//quiz/quizzes/user")
    suspend fun getUserQuizzes(): List<QuizResponseApi>

    @POST("/quizapp/quiz")
    suspend fun addQuiz(@Body quizRequestApi: QuizRequestApi): Response<Unit>

    @GET("/quizapp/quiz/categories")
    suspend fun getCategories(): List<CategoryApi>

    @POST("/sendResult")
    suspend fun sendResult(result: List<QuestionAnswered>): Unit

    @POST("/getQuizDetails")
    suspend fun getQuizDetails(): List<Question>
}