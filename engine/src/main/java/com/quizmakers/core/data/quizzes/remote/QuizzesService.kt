package com.quizmakers.core.data.quizzes.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

//TODO After api will by ready replace body or query with correct data.
interface QuizzesService {
    @GET("/quizapp/quiz/quizzes")
    suspend fun getPublicQuizzes(): List<QuizResponseApi>

    @GET("/quizapp/quiz/quizzes/user")
    suspend fun getUserQuizzes(): List<QuizResponseApi>

    @GET("/quizapp/quiz")
    suspend fun getQuizByCode(@Header("quizCode") quizCode: String): QuizResponseApi

    @POST("/quizapp/quiz")
    suspend fun addQuiz(@Body quizRequestApi: QuizRequestApi): Response<Unit>

    @GET("/quizapp/quiz/categories")
    suspend fun getCategories(): List<CategoryApi>

    @POST("/quizapp/finish")
    suspend fun finishQuiz(@Body result: QuizResult): Response<Unit>

    @GET("/quizapp/start/{id}")
    suspend fun startQuiz(@Path("id") id: String): QuizResponse

    @POST("/quizapp/quiz/generate/fromTitle")
    suspend fun generateQuiz(@Body quizGenerateRequest: QuizGenerateRequest): Response<Unit>
}