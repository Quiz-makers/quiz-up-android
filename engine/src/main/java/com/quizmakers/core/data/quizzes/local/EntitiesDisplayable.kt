package com.quizmakers.core.data.quizzes.local

import android.graphics.Bitmap

data class QuizDisplayable(
    val quizId: Int,
    val title: String,
    val description: String,
    val questionDtoSet: List<QuestionDisplayable>
)

data class QuestionDisplayable(
    val questionId: Int,
    val question: String,
    val answers: List<AnswerDisplayable>,
    val image: Bitmap?
)

data class AnswerDisplayable(
    val id: Int,
    val answer: String,
    val isCorrect: Boolean
)

data class QuizGeneralDisplayable(
    val quizId: Int,
    val title: String,
    val description: String,
    val quizShareCode: String?,
)