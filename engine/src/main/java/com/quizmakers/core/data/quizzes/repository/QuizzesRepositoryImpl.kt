package com.quizmakers.core.data.quizzes.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.local.QuizGeneralDisplayable
import com.quizmakers.core.data.quizzes.local.toQuizDisplayable
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import com.quizmakers.core.domain.dashboard.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
class QuizzesRepositoryImpl(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper,
) : QuizzesRepository {
    override suspend fun getPublicQuizzes(): List<QuizGeneralDisplayable> {
        return callOrThrow(errorWrapper) {
            api.getPublicQuizzes().map { it.toQuizDisplayable() }
            //  .toMutableList().also { it.add(mockQuiz.toQuizDisplayable()) }
        }
    }

    override suspend fun getUserQuizzes(): List<QuizGeneralDisplayable> {
        return callOrThrow(errorWrapper) {
            api.getUserQuizzes().map { it.toQuizDisplayable() }
        }
    }

    override suspend fun getQuizByCode(code: String): QuizGeneralDisplayable =
        callOrThrow(errorWrapper) {
            api.getQuizByCode(quizCode = code).toQuizDisplayable()
        }

}