package com.quizmakers.core.data.quizzes.remote

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

//Quiz Start
data class QuizResponse(
    val quizId: Int,
    val title: String,
    val description: String,
    val questionDtoSet: List<QuestionApi>
)

data class QuestionApi(
    val questionId: Int,
    val question: String,
    val answerDtoSet: List<AnswerApi>,
    @SerializedName("image") val imageBase64: String? = null
)

//
data class AnswerApi(
    val id: Int,
    val answer: String,
    val isCorrect: Boolean
)

data class QuizRequestApi(
    val title: String,
    val metaTitle: String,
    val summary: String,
    val description: String,
    val type: Int,
    val categoryId: Int,
    val score: Int,
    val publicAvailable: Boolean,
    @SerializedName("quizQuestionsWithAnswersEntities")
    val quizQuestions: Map<String, QuizQuestionApi>
)

data class QuizQuestionApi(
    val type: String,
    val question: String,
    @SerializedName("questionImage")
    val questionImages: String,
    val score: Int,
    @SerializedName("difficultyLevel")
    val difficulty: Int,
    @SerializedName("visibleInQuiz")
    val visible: Boolean,
    @SerializedName("questionAnswersEntities")
    val questionAnswers: List<QuestionAnswer>
)

data class QuestionAnswer(
    val answer: String,
    val correct: Boolean,
    val active: Boolean
)

data class QuizResponseApi(
    val type: String?,
    val category: String?,
    val title: String,
    val createdDate: String?,
    val quizId: Int,
    val score: Int?,
    val publicAvailable: Boolean?,
    val metaTitle: String?,
    val slug: String?,
    val updatedAt: String?,
    val publicFrom: String?,
    val startsAt: String?,
    val endsAt: String?,
    val summary: String?,
    val ownerName: String?,
    val ownerSurname: String?,
    val quizTime: Int?,
    val description: String,
)

data class QuestionsRequestApi(
    val questions: List<String>,
    val image: List<String?>,
    val answers: List<List<String>>,
    val correctAnswers: List<Int>,
)

data class CategoryApi(
    val categoryId: Int,
    val category: String,
    val thumbnail: String
)

fun QuestionsRequestApi.toQuizQuestionApi(): Map<String, QuizQuestionApi> =
    questions.indices.associate { i ->
        "q$i" to QuizQuestionApi(
            type = "1",
            question = questions[i],
            questionImages = image[i] ?: "",
            score = 1,
            difficulty = 1,
            visible = true,
            questionAnswers = Pair(correctAnswers[i], answers[i]).toQuestionAnswer()
        )
    }

private fun Pair<Int, List<String>>.toQuestionAnswer(): List<QuestionAnswer> =
    second.mapIndexed { index, answer ->
        QuestionAnswer(
            answer = answer,
            correct = index == first,
            active = true
        )
    }


data class QuizResult(
    val quizId: Int,
    val answerDtoList: List<AnswerDto>,
    val startTime: LocalDateTime,
    val finishTime: LocalDateTime
)

data class AnswerDto(
    val questionId: Int,
    val answerId: Int?
)

data class AuthResponseApi(
    val name: String?,
    val surname: String?,
    val userName: String
)
data class QuizGenerateRequest(
    val title: String,
    val type: Int,
    val categoryId: Int,
    val numberOfQuestions: Int,
    val answersPerQuestion: Int,
    val publicAvailable: Boolean,
    val quizTime: String?,
    val startsAt: String?,
    val endsAt: String?
)