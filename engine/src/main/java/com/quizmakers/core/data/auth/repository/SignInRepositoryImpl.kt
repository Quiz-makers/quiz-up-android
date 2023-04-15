package com.quizmakers.core.data.auth.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserAuthenticateRequest
import com.quizmakers.core.data.quizzes.remote.AuthResponseApi
import com.quizmakers.core.domain.auth.repository.SignInRepository
import com.quizmakers.core.domain.session.SessionManager
import okhttp3.Headers
import org.koin.core.annotation.Factory
import retrofit2.HttpException
import retrofit2.Response

@Factory
class SignInRepositoryImpl(
    private val api: AuthService,
    private val errorWrapper: ErrorWrapper,
    private val sessionManager: SessionManager
) : SignInRepository {

    override suspend fun signIn(email: String, password: String) {
        callOrThrow(errorWrapper) {
            api.signIn(UserAuthenticateRequest(email = email.trim(), password = password))
        }.also {
            if (!it.isSuccessful) {
                throw errorWrapper.wrap(HttpException(it))
            } else {
                saveAuthorizationHeader(it)
                it.body()?.let { auth -> sessionManager.saveUserName(auth.userName) }
            }
        }
    }

    private fun saveAuthorizationHeader(response: Response<AuthResponseApi>) {
        val headers: Headers = response.headers()
        sessionManager.saveToken(headers["Authorization"].toString())
    }
}