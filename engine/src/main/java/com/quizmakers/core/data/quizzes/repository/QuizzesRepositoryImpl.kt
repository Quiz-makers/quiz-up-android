package com.quizmakers.core.data.quizzes.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.QuizResponseApi
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import com.quizmakers.core.domain.dashboard.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
class QuizzesRepositoryImpl(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper,
) : QuizzesRepository {
    override suspend fun getPublicQuizzes(): List<QuizResponseApi> {
        return callOrThrow(errorWrapper) {
            api.getPublicQuizzes()
        }
    }

    override suspend fun getUserQuizzes(): List<QuizResponseApi> {
        return callOrThrow(errorWrapper) {
            api.getUserQuizzes()
        }
    }

    override suspend fun getQuizByCode(code: String): QuizResponseApi =
        callOrThrow(errorWrapper) {
            api.getQuizByCode(quizCode = code)
        }

}