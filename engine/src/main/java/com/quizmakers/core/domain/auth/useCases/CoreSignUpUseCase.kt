package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.domain.auth.repository.SignOutRepository
import org.koin.core.annotation.Factory

@Factory
open class CoreSignUpUseCase(private val signOutRepository: SignOutRepository) {
    suspend operator fun invoke(
        name: String, surname: String, userName: String, email: String, password: String
    ) = signOutRepository.signOut(
        name = name,
        surname = surname,
        userName = userName,
        email = email,
        password = password
    )
}