package com.quizmakers.core.data.quizzes.repository

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import com.quizmakers.core.domain.quizzes.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
class QuizzesRepositoryImpl(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper,
) : QuizzesRepository {
    override suspend fun getQuizzes(): List<String> {
        return callOrThrow(errorWrapper) {
            api.getQuizzes()
        }
    }
}