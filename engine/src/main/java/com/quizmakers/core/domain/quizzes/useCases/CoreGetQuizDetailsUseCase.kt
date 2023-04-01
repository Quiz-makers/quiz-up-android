package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.data.quizzes.remote.Question
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory

@Factory
class CoreGetQuizDetailsUseCase(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke() = questionsMocks
}

private val questionsMocks = listOf(
    Question(
        "What is the capital city of France?",
        listOf("Paris", "Madrid", "Rome", "Berlin"),
        "Paris"
    ),
    Question(
        "What is the highest mountain in the world?",
        listOf("Mount Everest", "K2", "Kangchenjunga", "Lhotse"),
        "Mount Everest"
    ),
    Question(
        "What is the largest country in the world by land area?",
        listOf("Russia", "Canada", "China", "United States"),
        "Russia"
    )
)