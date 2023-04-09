package com.quizmakers.core.domain.quizzes.useCases

import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.api.exception.callOrThrow
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import org.koin.core.annotation.Factory

@Factory
class CoreGetCategoriesUseCase(
    private val api: QuizzesService,
    private val errorWrapper: ErrorWrapper
) {
    suspend operator fun invoke() = callOrThrow(errorWrapper) { api.getCategories() }
}

private val mockCategories = listOf(
    CategoryApi(
        categoryId = 1,
        category = "Geografia",
        thumbnail = "bnVsbA=="
    ),
    CategoryApi(
        categoryId = 2,
        category = "Matematyka",
        thumbnail = "bnVsbA=="
    ),
    CategoryApi(
        categoryId = 3,
        category = "Historia",
        thumbnail = "bnVsbA=="
    ),
    CategoryApi(
        categoryId = 4,
        category = "Sztuka",
        thumbnail = "bnVsbA=="
    )
)