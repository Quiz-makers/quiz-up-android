package com.quizmakers.core.domain.battle.useCases
import com.quizmakers.core.domain.session.SessionManager
import org.koin.core.annotation.Factory

@Factory
class CoreGetUserNameUseCase(private val sessionManager: SessionManager) {
    operator fun invoke() = sessionManager.getUserName()
}