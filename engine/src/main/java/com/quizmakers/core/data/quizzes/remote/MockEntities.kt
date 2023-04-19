package com.quizmakers.core.data.quizzes.remote

var mockQuiz = QuizResponseApi(
    type = "multiple_choice",
    category = "history",
    title = "World War II Quiz",
    createdDate = "2022-03-12T10:30:00Z",
    quizId = 12345,
    score = 85,
    publicAvailable = true,
    metaTitle = "Test your knowledge on World War II",
    slug = "world-war-ii-quiz",
    updatedAt = "2022-03-15T08:45:00Z",
    publicFrom = "2022-03-20T12:00:00Z",
    startsAt = "2022-03-22T10:00:00Z",
    endsAt = "2022-03-22T12:00:00Z",
    summary = "Test your knowledge on World War II with this quiz",
    ownerName = "John",
    ownerSurname = "Doe",
    quizTime = 20,
    description = "This quiz will test your knowledge on the major events and figures of World War II.",
)
val mockedQuestion = listOf(
    QuestionApi(
        2, "What is the highest mountain in the world?", listOf(
            AnswerApi(0, "Mount Everest", true),
            AnswerApi(1, "K2", false),
            AnswerApi(2, "Kangchenjunga", false),
            AnswerApi(3, "Lhotse", false)
        ), imageBase64 = mock
    ),
    QuestionApi(
        3, "What is the largest country in the world by land area?", listOf(
            AnswerApi(0, "Russia", true),
            AnswerApi(1, "Canada", false),
            AnswerApi(2, "China", false),
            AnswerApi(3, "United States", false),
        ), imageBase64 = mock

    ),
)