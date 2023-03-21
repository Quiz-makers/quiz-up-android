package com.quizmakers.core.data.auth.remote

data class UserRegisterRequest(
    var name: String,
    var surname: String,
    var userName: String,
    var email: String,
    var password: String,
)

data class UserAuthenticateRequest(
    var email: String,
    var password: String,
)
