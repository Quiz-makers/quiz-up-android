package com.quizmakers.core.domain.auth.repository

interface SignOutRepository {
    suspend fun signOut(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    )
}