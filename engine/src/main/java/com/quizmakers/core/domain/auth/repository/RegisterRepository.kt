package com.quizmakers.core.domain.auth.repository

interface RegisterRepository {
    suspend fun register(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    )
}