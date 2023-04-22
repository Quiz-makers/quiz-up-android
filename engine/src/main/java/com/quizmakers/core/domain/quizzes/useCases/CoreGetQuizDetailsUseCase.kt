package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.local.toQuizDisplayable
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory

@Factory
class CoreGetQuizDetailsUseCase(
    private val api: QuizzesService, private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke(id: String) = callOrThrow(errorWrapper) {
        api.startQuiz(id = id).toQuizDisplayable()
    }
}
