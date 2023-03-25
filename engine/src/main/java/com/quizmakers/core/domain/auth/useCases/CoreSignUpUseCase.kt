package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserRegisterRequest
import org.koin.core.annotation.Factory

@Factory
open class CoreSignUpUseCase(private val api: AuthService) {
    suspend operator fun invoke(
        name: String, surname: String, userName: String, email: String, password: String
    ) = api.signUp(UserRegisterRequest(name, surname, userName, email, password))
}