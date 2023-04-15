package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.QuizResult
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory

@Factory
class CoreSendQuizResultUseCase(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke(quizResult: QuizResult) {
        return callOrThrow(errorWrapper) {
            api.finishQuiz(
                result = quizResult
            ).body()
        }
    }
}