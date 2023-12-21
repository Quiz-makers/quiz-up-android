package com.quizmakers.core.data.battle.remote

/**
 * Is Global Data, needed to check server event
 */
data class ServerMessage(
    val serverInfo: String
)

/**
 * Is Data for :
 * @see ServerInfo.IN_BATTLE_ROOM,
 * @see ServerInfo.IN_WAITING_ROOM,
 * @see ServerInfo.ERROR_MESSAGE,
 */
data class ContentInfoMessageState(
    val serverInfo: String,
    val content: String? = null,
    val date: String? = null,
)

/**
 * Is Data for :
 * @see ServerInfo.BATTLE_RESULT,
 */
data class BattleResultMessageState(
    val serverInfo: String,
    val content: BattleResultResponse,
    val date: String,
)

/**
 * Is Data for :
 * @see ServerInfo.ANSWER_RESULT,
 */
data class AnswerMessageState(
    val serverInfo: String,
    val content: QuestionResultResponse,
    val date: String,
)

/**
 * Is Data for :
 * @see ServerInfo.QUESTION_MESSAGE,
 */
data class QuestionMessageState(
    val serverInfo: String,
    val content: QuestionResponse,
    val date: String,
)

data class QuestionResponse(
    val id: Long,
    val question: String,
    val answers: List<AnswerResponse>
)

data class AnswerResponse(
    val id: Long,
    val answer: String,
    val isCorrect: Boolean
)

data class BattleResultResponse(
    val questionResults: List<QuestionResultResponse>
)

data class QuestionResultResponse(
    val resultList: Map<String, Result>
)

data class Result(
    val questionId: Long,
    val answerId: Long? = null,
    val correct: Boolean
)

data class AnswerMessage(
    val id: String,
    val answer: String,
)

fun String.toServerInfo(): ServerInfo = ServerInfo.valueOf(this)