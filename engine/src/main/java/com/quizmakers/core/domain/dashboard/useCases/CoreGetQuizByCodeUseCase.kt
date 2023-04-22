package com.quizmakers.core.domain.dashboard.useCases

import com.quizmakers.core.domain.dashboard.repository.QuizzesRepository
import org.koin.core.annotation.Factory

@Factory
class CoreGetQuizByCodeUseCase(
    private val quizzesRepository: QuizzesRepository
) {
    suspend operator fun invoke(code: String) = quizzesRepository.getQuizByCode(code = code)
}