package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.domain.quizzes.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
open class CoreGetQuizzesUseCase(
    private val quizzesRepository: QuizzesRepository
) {
    suspend operator fun invoke() = generateMock()
       // quizzesRepository.getQuizzes()
}
private fun generateMock(): List<String> {
    return MutableList(4) { index ->
        "Informatyka"
    }
}