package com.quizmakers.core.domain.auth.useCases

import com.quizmakers.core.domain.auth.repository.RegisterRepository
import org.koin.core.annotation.Factory

@Factory
open class CoreRegisterUseCase(private val registerRepository: RegisterRepository) {
    suspend operator fun invoke(
        name: String, surname: String, userName: String, email: String, password: String
    ) = registerRepository.register(
        name = name,
        surname = surname,
        userName = userName,
        email = email,
        password = password
    )
}