package com.quizmakers.core.data.auth.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserRegisterRequest
import com.quizmakers.core.domain.auth.repository.SignOutRepository
import com.quizmakers.core.domain.session.SessionManager
import okhttp3.Headers
import org.koin.core.annotation.Factory
import retrofit2.Response

@Factory
class SignOutRepositoryImpl(
    private val api: AuthService,
    private val errorWrapper: ErrorWrapper,
    private val sessionManager: SessionManager

) : SignOutRepository {

    override suspend fun signOut(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    ) {
        callOrThrow(errorWrapper) {
            api.signUp(UserRegisterRequest(name, surname, userName, email, password))
        }.also {
            saveAuthorizationHeader(it)
        }
    }

    private fun saveAuthorizationHeader(response: Response<Unit>) {
        val headers: Headers = response.headers()
        sessionManager.saveToken(headers["Authorization"].toString())
    }
}