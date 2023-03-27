package com.quizmakers.core.domain.session

interface SessionManager {
    fun getToken(): String?
    fun saveToken(token: String)
    fun deleteToken()
}