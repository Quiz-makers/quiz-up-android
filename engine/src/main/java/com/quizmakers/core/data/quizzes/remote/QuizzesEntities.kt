package com.quizmakers.core.data.quizzes.remote


data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val imageUrl: String? = "https://fajnepodroze.pl/wp-content/uploads/2020/03/Krajobraz.jpg"
)

data class QuestionAnswered(
    val quizId: String,
    val optionId: String
)