package com.quizmakers.core.domain.auth.repository

interface SignInRepository {
    suspend fun signIn(email: String, password: String)
}