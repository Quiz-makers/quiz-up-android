package com.quizmakers.core.domain.dashboard.repository

import com.quizmakers.core.data.quizzes.remote.QuizResponseApi

interface QuizzesRepository {
    suspend fun getPublicQuizzes(): List<QuizResponseApi>
    suspend fun getUserQuizzes(): List<QuizResponseApi>
    suspend fun getQuizByCode(code: String): QuizResponseApi
}