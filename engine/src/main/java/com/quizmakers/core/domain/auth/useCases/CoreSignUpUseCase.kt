package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserRegisterRequest
import org.koin.core.annotation.Factory

@Factory
open class CoreSignUpUseCase(private val api: AuthService) {
    suspend operator fun invoke(userRegister: UserRegisterRequest) =
        api.signUp(userRegister)
}