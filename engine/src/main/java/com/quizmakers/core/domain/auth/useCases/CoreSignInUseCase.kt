package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserAuthenticateRequest
import org.koin.core.annotation.Factory

@Factory
open class CoreSignInUseCase(private val api: AuthService) {
    suspend operator fun invoke(userAuthenticate: UserAuthenticateRequest) =
        api.signIn(userAuthenticate)
}