package com.quizmakers.quizup.core.domain

import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.domain.auth.useCases.CoreSignInUseCase
import org.koin.core.annotation.Factory

@Factory
class SignInUseCase(private val api: AuthService) : CoreSignInUseCase(api = api)