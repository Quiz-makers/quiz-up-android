package com.quizmakers.core.domain.dashboard.repository

import com.quizmakers.core.data.quizzes.local.QuizGeneralDisplayable

interface QuizzesRepository {
    suspend fun getPublicQuizzes(): List<QuizGeneralDisplayable>
    suspend fun getUserQuizzes(): List<QuizGeneralDisplayable>
    suspend fun getQuizByCode(code: String): QuizGeneralDisplayable
}