package com.quizmakers.quizup.presentation.battle

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.quizmakers.core.data.battle.remote.AnswerResponse
import com.quizmakers.core.data.battle.remote.QuestionResponse
import com.quizmakers.core.data.battle.remote.ServerInfo
import com.quizmakers.core.data.battle.remote.toServerInfo
import com.quizmakers.core.domain.battle.useCases.TimerState
import com.quizmakers.quizup.R
import com.quizmakers.quizup.presentation.battle.data.AnswerResultState
import com.quizmakers.quizup.presentation.battle.data.BattleResult
import com.quizmakers.quizup.presentation.battle.data.GameResult
import com.quizmakers.quizup.presentation.battle.data.ResultSummarisingState
import com.quizmakers.quizup.presentation.battle.data.getBattleResult
import com.quizmakers.quizup.presentation.battle.data.getScoreResult
import com.quizmakers.quizup.presentation.battle.providers.BattlePreviewParameterProvider
import com.quizmakers.quizup.presentation.battle.providers.BattleResultPreviewParameterProvider
import com.quizmakers.quizup.ui.common.BaseButton
import com.quizmakers.quizup.ui.common.SnackbarHandler
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.GreenSuccess
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.quizmakers.quizup.ui.theme.RedError
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

const val playerIcon = "🙎🏼‍"
const val opponentIcon = "🙎🏼‍"
const val opponentIconAnswered = "🙋"

@Destination
@Composable
fun BattleScreen(
    navigator: DestinationsNavigator,
    battleViewModel: BattleViewModel = koinViewModel { parametersOf("") },
    snackbarHandler: SnackbarHandler
) {
    LaunchedEffect(Unit) {
        battleViewModel.stateError.collect {
            it?.let {
                snackbarHandler.showErrorSnackbar(message = it.content)
            }
        }
    }
    DisposableEffect(key1 = true) {
        onDispose {
            battleViewModel.onClear()
        }
    }
    val serverState = battleViewModel.stateServer.collectAsStateWithLifecycle().value
    val battleResultState = battleViewModel.battleResultState.collectAsStateWithLifecycle().value
    val answerResultState = battleViewModel.answerResultState.collectAsStateWithLifecycle().value
    val timerState = battleViewModel.timerState.collectAsStateWithLifecycle().value
    val scoreState = battleViewModel.scoreState.collectAsStateWithLifecycle().value
    val stateQuestion = battleViewModel.stateQuestion.collectAsStateWithLifecycle().value?.content

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = navigator::navigateUp, modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Icons.Default.Close.name,
                    tint = DarkBlue,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        when (serverState.serverInfo.toServerInfo()) {
            ServerInfo.IN_WAITING_ROOM -> LoadingScreen()
            ServerInfo.IN_BATTLE_ROOM -> BattleRoomScreen(
                battleResultState = battleResultState,
                answerResultState = answerResultState,
                stateQuestion = stateQuestion,
                scoreState = scoreState,
                timerState = timerState,
                sendAnswer = { id, answer -> battleViewModel.emitAnswer(id, answer) },
                launchTimer = battleViewModel::startTimerTick
            )

            else -> BaseScreen(startBattle = battleViewModel::startBattle)
        }

    }
}

@Composable
private fun BaseScreen(startBattle: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.battle_blue))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(composition, modifier = Modifier.size(300.dp))
        Text(
            text = stringResource(R.string.welcome_title_battle_mode),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.search_player_for),
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(20.dp))
        BaseButton(label = stringResource(R.string.search_opponent), onClick = startBattle)
    }
}

@Composable
private fun LoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(composition, modifier = Modifier.size(300.dp), progress = { progress })
        Text(
            text = stringResource(R.string.welcome_title_battle_mode),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.search_for_player),
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun BattleRoomScreen(
    battleResultState: ResultSummarisingState,
    answerResultState: AnswerResultState,
    stateQuestion: QuestionResponse?,
    scoreState: GameResult?,
    sendAnswer: (Long, String) -> Unit,
    timerState: TimerState,
    launchTimer: (Int) -> Unit
) {
    LaunchedEffect(stateQuestion) {
        stateQuestion?.let {
            launchTimer(10)
        } ?: run {
            launchTimer(5)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (stateQuestion == null) {
            when (timerState) {
                TimerState.None -> Unit
                is TimerState.Running -> Text(
                    text = "${timerState.sec}",
                    color = DarkBlue,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        when (battleResultState) {
            is ResultSummarisingState.ResultReady -> ResultScreen(
                battleResult = battleResultState.gameResult.getBattleResult(),
                playerName = battleResultState.gameResult.me.name,
                opponentName = battleResultState.gameResult.opponent.name,
                result = battleResultState.gameResult.getScoreResult()
            )

            ResultSummarisingState.None -> GameFlowScreen(
                stateQuestion = stateQuestion,
                answerResultState = answerResultState,
                scoreState = scoreState,
                timerState = timerState,
                sendAnswer = sendAnswer,
            )
        }


    }
}

@Composable
private fun GameFlowScreen(
    stateQuestion: QuestionResponse?,
    answerResultState: AnswerResultState,
    scoreState: GameResult?,
    timerState: TimerState,
    sendAnswer: (Long, String) -> Unit
) {
    val mySelectedAnswer = remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(answerResultState) {
        when (answerResultState) {
            is AnswerResultState.AnswerResult -> mySelectedAnswer.value = null
            AnswerResultState.None -> Unit
        }
    }
    Column {
        stateQuestion?.question?.let { question ->
            QuestionCard(question,
                scoreState?.let { "${it.me.score} - ${it.opponent.score}" } ?: "0 - 0")
        }
        Spacer(modifier = Modifier.height(8.dp))
        stateQuestion?.answers?.forEach { answer ->
            AnswerCard(answer, mySelectedAnswer.value == answer.id, answerResultState) {
                if (mySelectedAnswer.value == null) {
                    sendAnswer(answer.id, answer.answer)
                    mySelectedAnswer.value = answer.id
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
        stateQuestion?.let {
            when (timerState) {
                TimerState.None -> Unit
                is TimerState.Running -> TimerIndicator((timerState.sec / 10.0).toFloat())
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun TimerIndicator(indicatorProgress: Float) {
    val progress = remember { mutableStateOf(0f) }
    val progressAnimDuration = 1
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress, animationSpec = tween(
            durationMillis = progressAnimDuration, easing = FastOutLinearInEasing
        ), label = ""
    )
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)), // Rounded edges
        progress = progressAnimation
    )
    LaunchedEffect(indicatorProgress) {
        progress.value = indicatorProgress
    }
}

@Composable
private fun QuestionCard(
    question: String, result: String
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = playerIcon, fontSize = 35.sp)
            Text(text = opponentIcon, fontSize = 35.sp)
        }
        Box {
            Card(elevation = 10.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 150.dp)
                        .padding(vertical = 20.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = question,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .background(color = DarkBlue)
                    .padding(horizontal = 20.dp, vertical = 2.dp)
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = result,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PlayerNames(playerName: String, opponentName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center
    ) {
        Text(text = playerName, fontSize = 18.sp, textAlign = TextAlign.Center)
        Text(text = playerIcon, fontSize = 35.sp)
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            modifier = Modifier.offset(y = 10.dp),
            text = "vs",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = opponentIcon, fontSize = 35.sp)
        Text(text = opponentName, fontSize = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ResultScreen(
    battleResult: BattleResult, playerName: String, opponentName: String, result: String
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (battleResult) {
                BattleResult.Draw -> R.raw.battle
                BattleResult.Loser -> R.raw.lost_battle
                BattleResult.Winner -> R.raw.winner
            }
        )
    )
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(composition, modifier = Modifier.size(300.dp), progress = { progress })
        PlayerNames(playerName, opponentName)
        Column(
            modifier = Modifier
                .background(color = DarkBlue)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = result,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (battleResult) {
                BattleResult.Draw -> stringResource(R.string.draw)
                BattleResult.Loser -> stringResource(R.string.loser)
                BattleResult.Winner -> stringResource(R.string.winner)
            }, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center,
            text = when (battleResult) {
                BattleResult.Draw -> stringResource(R.string.draw_info)
                BattleResult.Loser -> stringResource(R.string.loser_info)
                BattleResult.Winner -> stringResource(R.string.winner_info)
            },
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun AnswerCard(
    answer: AnswerResponse,
    isSelected: Boolean,
    answerResultState: AnswerResultState,
    onSelectedAnswer: () -> Unit,
) {
    when (answerResultState) {
        is AnswerResultState.AnswerResult ->
            Card(elevation = 10.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = if (answer.isCorrect) GreenSuccess else if (answerResultState.answerResult.myAnswer == answer.id) RedError else Color.White)
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = answer.answer,
                            maxLines = 1,
                            color = if (answer.isCorrect) Color.White else Color.Black
                        )
                        if (answerResultState.answerResult.opponentAnswer == answer.id) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = opponentIconAnswered)
                        }
                    }
                }
            }

        AnswerResultState.None -> Card(elevation = 10.dp) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = if (isSelected) DarkBlue else Color.White)
                .clickable { onSelectedAnswer() }
                .padding(vertical = 14.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.Start) {
                Text(
                    text = answer.answer,
                    maxLines = 1,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun BattleRoomScreenPreview(
    @PreviewParameter(BattlePreviewParameterProvider::class) questionResponse: QuestionResponse
) {
    QuizUpTheme {
        BattleRoomScreen(battleResultState = ResultSummarisingState.None,
            answerResultState = AnswerResultState.None,
            stateQuestion = questionResponse,
            scoreState = null,
            sendAnswer = { _, _ -> },
            timerState = TimerState.None,
            launchTimer = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun BattleResultScreenPreview(
    @PreviewParameter(BattleResultPreviewParameterProvider::class) battleResult: BattleResult
) {
    QuizUpTheme {
        ResultScreen(battleResult, "", "", "1 - 1")
    }
}