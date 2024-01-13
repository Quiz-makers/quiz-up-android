package com.quizmakers.core.data.quizzes.local

import com.quizmakers.core.data.quizzes.remote.QuizResponse
import com.quizmakers.core.data.quizzes.remote.QuizResponseApi
import com.quizmakers.core.utils.decodeImage

fun QuizResponse.toQuizDisplayable() = QuizDisplayable(
    quizId = quizId,
    title = title,
    description = description,
    questionDtoSet = questionDtoSet.map {
        QuestionDisplayable(
            questionId = it.questionId,
            question = it.question,
            answers = it.answerDtoSet.map { answer ->
                AnswerDisplayable(
                    id = answer.id,
                    answer = answer.answer,
                    isCorrect = answer.isCorrect,
                )
            },
            image = it.imageBase64?.takeIf { image -> image.isNotBlank() && image != "null" }
                ?.decodeImage()
        )
    })

fun QuizResponseApi.toQuizDisplayable(isExpandedMode: Boolean) = QuizGeneralDisplayable(
    quizId = quizId,
    title = title,
    quizShareCode = if (isExpandedMode) quizCode else null,
    description = description,
)