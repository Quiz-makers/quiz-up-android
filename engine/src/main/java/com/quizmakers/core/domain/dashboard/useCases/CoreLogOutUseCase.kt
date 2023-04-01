package com.quizmakers.core.domain.dashboard.useCases

import com.quizmakers.core.domain.dashboard.repository.LogOutRepository
import org.koin.core.annotation.Factory

@Factory
open class CoreLogOutUseCase(private val logOutRepository: LogOutRepository) {
    operator fun invoke() = logOutRepository.logOut()
}