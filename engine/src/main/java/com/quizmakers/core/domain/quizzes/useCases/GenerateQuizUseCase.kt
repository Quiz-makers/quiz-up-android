package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.QuizGenerateRequest
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory
import retrofit2.HttpException

@Factory
class GenerateQuizUseCase(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke(quizGenerateRequest: QuizGenerateRequest) =
        callOrThrow(errorWrapper) { api.generateQuiz(quizGenerateRequest) }.let {
            if (!it.isSuccessful) {
                throw errorWrapper.wrap(HttpException(it))
            }
        }
}
