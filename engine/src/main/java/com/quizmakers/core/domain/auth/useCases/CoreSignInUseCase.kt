package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.domain.auth.repository.SignInRepository
import org.koin.core.annotation.Factory

@Factory
open class CoreSignInUseCase(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(email: String, password: String) =
        signInRepository.signIn(email, password)

}