package com.quizmakers.core.domain.dashboard.repository

interface QuizzesRepository {
    suspend fun getQuizzes(): List<String>
}