package com.quizmakers.core.data.auth.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserRegisterRequest
import com.quizmakers.core.domain.auth.repository.RegisterRepository
import com.quizmakers.core.domain.session.SessionManager
import okhttp3.Headers
import org.koin.core.annotation.Factory
import retrofit2.Response

@Factory
class RegisterRepositoryImpl(
    private val api: AuthService,
    private val errorWrapper: ErrorWrapper

) : RegisterRepository {

    override suspend fun register(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    ) {
        callOrThrow(errorWrapper) {
            api.register(UserRegisterRequest(name, surname, userName, email, password))
        }
    }
}