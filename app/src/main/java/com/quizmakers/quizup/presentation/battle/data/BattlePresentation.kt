package com.quizmakers.quizup.presentation.battle.data

sealed interface BattleResult {
    object Winner : BattleResult
    object Loser : BattleResult
    object Draw : BattleResult
}

sealed class ResultSummarisingState {
    object None : ResultSummarisingState()
    data class ResultReady(val gameResult: GameResult) : ResultSummarisingState()
}

sealed class AnswerResultState {
    object None : AnswerResultState()
    data class AnswerResult(val answerResult: AnswerPlayerResult) : AnswerResultState()
}

data class AnswerPlayerResult(
    val myAnswer: Long?,
    val opponentAnswer: Long?
)


data class GameResult(
    val me: Player, val opponent: Player
)

fun GameResult.getBattleResult(): BattleResult =
    if (me.score > opponent.score) BattleResult.Winner
    else if (me.score == opponent.score) BattleResult.Draw
    else BattleResult.Loser

fun GameResult.getScoreResult(): String = "${me.score} - ${opponent.score}"

data class Player(
    val name: String,
    val score: Int
)