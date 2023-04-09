package com.quizmakers.core.domain.dashboard.useCases

import com.quizmakers.core.domain.dashboard.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
class CoreGetUserQuizzesUseCase(
    private val quizzesRepository: QuizzesRepository
) {
    suspend operator fun invoke() = quizzesRepository.getUserQuizzes()
}

private fun generateMock(): List<String> {
    return MutableList(4) { index ->
        "Geografia"
    }
}