package com.quizmakers.core.domain.quizzes.repository

interface QuizzesRepository {
    suspend fun getQuizzes(): List<String>
}