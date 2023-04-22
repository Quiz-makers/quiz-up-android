package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.QuizRequestApi
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory
import retrofit2.HttpException

@Factory
class CoreAddNewQuizUseCase(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke(quizRequestApi: QuizRequestApi) =
        callOrThrow(errorWrapper) { api.addQuiz(quizRequestApi) }.let {
            if (!it.isSuccessful) {
                throw errorWrapper.wrap(HttpException(it))
            }
        }
}

