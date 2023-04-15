package com.quizmakers.core.domain.session

interface SessionManager {
    fun getToken(): String?
    fun getUserName(): String
    fun saveToken(token: String)
    fun saveUserName(userName: String)
    fun deleteToken()
}