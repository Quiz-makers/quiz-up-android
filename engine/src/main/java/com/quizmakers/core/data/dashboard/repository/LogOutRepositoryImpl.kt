package com.quizmakers.core.data.dashboard.repository

import com.quizmakers.core.domain.dashboard.repository.LogOutRepository
import com.quizmakers.core.domain.session.SessionManager
import org.koin.core.annotation.Factory

@Factory
class LogOutRepositoryImpl(
    private val sessionManager: SessionManager
) : LogOutRepository {
    override fun logOut() = sessionManager.deleteToken()

}