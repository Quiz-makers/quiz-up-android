package com.quizmakers.quizup.presentation.battle.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.quizmakers.core.data.battle.remote.AnswerResponse
import com.quizmakers.core.data.battle.remote.QuestionResponse
import com.quizmakers.quizup.presentation.battle.data.BattleResult

class BattlePreviewParameterProvider :
    PreviewParameterProvider<QuestionResponse> {
    override val values = sequenceOf(
        QuestionResponse(
            id = 10,
            question = "Tutaj będzie zawarte pytanie które będzie zawierać jedną odpowiedź prawidłową?",
            answers = listOf(
                AnswerResponse(
                    id = 1,
                    answer = "Pomidor",
                    isCorrect = true
                ),
                AnswerResponse(
                    id = 1,
                    answer = "Pomidor",
                    isCorrect = false
                ),
                AnswerResponse(
                    id = 1,
                    answer = "Pomidor",
                    isCorrect = false
                ),
                AnswerResponse(
                    id = 1,
                    answer = "Pomidor",
                    isCorrect = false
                )
            )
        )
    )
}
class BattleResultPreviewParameterProvider :
    PreviewParameterProvider<BattleResult> {
    override val values = sequenceOf(
        BattleResult.Draw,
        BattleResult.Loser,
        BattleResult.Winner,
    )
    }