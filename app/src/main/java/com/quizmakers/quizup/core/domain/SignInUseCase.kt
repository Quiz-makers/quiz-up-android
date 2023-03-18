package com.quizmakers.quizup.core.domain

import com.quizmakers.quizup.core.api.QuizUpApiService
import org.koin.core.annotation.Factory

@Factory
class SignInUseCase(private val api: QuizUpApiService) {
    suspend operator fun invoke() = api.signIn("mockName")
}