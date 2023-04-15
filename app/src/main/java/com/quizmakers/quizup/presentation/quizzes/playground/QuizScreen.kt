package com.quizmakers.quizup.presentation.quizzes.playground

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.quizmakers.core.data.quizzes.remote.AnswerApi
import com.quizmakers.core.data.quizzes.remote.AnswerDto
import com.quizmakers.core.data.quizzes.remote.QuestionApi
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.ui.common.*
import com.quizmakers.quizup.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun QuizScreen(
    navigator: DestinationsNavigator,
    snackbarHandler: SnackbarHandler,
    quizId: String,
    quizScreenViewModel: QuizScreenViewModel = koinViewModel { parametersOf(quizId) }
) {
    LaunchedEffect(Unit) {
        quizScreenViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> {
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }
                BaseViewModel.MessageEvent.Success -> Unit
            }
        }
    }
    QuizScreen(
        closeQuiz = navigator::navigateUp,
        quizState = quizScreenViewModel.quizState.collectAsStateWithLifecycle().value,
        refresh = quizScreenViewModel::getQuiz,
        addAnswer = quizScreenViewModel::addAnswer,
        finishQuiz = quizScreenViewModel::finishQuiz
    )
}

@Composable
private fun QuizScreen(
    closeQuiz: () -> Unit,
    quizState: QuizScreenViewModel.QuizState,
    refresh: () -> Unit,
    addAnswer: (AnswerDto) -> Unit,
    finishQuiz: () -> Unit,
) {
    val currentQuestionIndex = remember { mutableStateOf(0) }
    val selectedAnswer = remember { mutableStateOf(-1) }
    val isAnswered = remember { mutableStateOf(false) }
    val buttonLoadingState = remember { mutableStateOf(false) }
    val quizJustEnd = remember { mutableStateOf(false) }
    val score = remember { mutableStateOf(0) }
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
                onClick = closeQuiz,
                modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Icons.Default.Close.name,
                    tint = DarkBlue,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        when (quizState) {
            is QuizScreenViewModel.QuizState.Error -> ErrorScreen {
                refresh()
            }
            QuizScreenViewModel.QuizState.Loading -> LoadingScreen()
            QuizScreenViewModel.QuizState.None -> Unit
            is QuizScreenViewModel.QuizState.Success -> {
                when (quizJustEnd.value) {
                    true -> QuizSummary(
                        score = score,
                        answersSize = quizState.quizzesList.size,
                        navToDashboardScreen = closeQuiz,
                        finishQuiz = finishQuiz
                    )
                    false ->
                        QuizLayout(
                            quizJustEnd = quizJustEnd,
                            currentQuestionIndex = currentQuestionIndex,
                            questionApi = quizState.quizzesList,
                            selectedAnswer = selectedAnswer,
                            isAnswered = isAnswered,
                            buttonLoadingState = buttonLoadingState,
                            score = score,
                            addAnswer = addAnswer
                        )
                }
            }
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun QuizSummary(
    score: MutableState<Int>,
    navToDashboardScreen: () -> Unit,
    answersSize: Int,
    finishQuiz: () -> Unit,

    ) {
    LaunchedEffect(Unit) {
        finishQuiz()
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.your_points), fontSize = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 50.sp,
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(
                                colors = listOf(MediumBlue, DarkBlue)
                            )
                        )
                    ) {
                        append("${score.value}/${answersSize}")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        BaseButtonWithIcon(
            label = stringResource(R.string.finish),
            icon = Icons.Default.Done,
            onClick = navToDashboardScreen,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun QuizLayout(
    currentQuestionIndex: MutableState<Int>,
    questionApi: List<QuestionApi>,
    selectedAnswer: MutableState<Int>,
    isAnswered: MutableState<Boolean>,
    buttonLoadingState: MutableState<Boolean>,
    score: MutableState<Int>,
    quizJustEnd: MutableState<Boolean>,
    addAnswer: (AnswerDto) -> Unit,
) {
    var count by remember { mutableStateOf(10) }
    val scope = rememberCoroutineScope()

    fun quizAnsweredAction() {

        addAnswer(
            AnswerDto(
                questionApi[currentQuestionIndex.value].questionId,
                selectedAnswer.value
            )
        )
        isAnswered.value = true
        buttonLoadingState.value = true
        if (selectedAnswer.value == questionApi[currentQuestionIndex.value].correctAnswer) {
            score.value++
        }
        scope.launch {
            count = -1
            delay(2000)
            count = 10
            buttonLoadingState.value = false
            isAnswered.value = false
            if (currentQuestionIndex.value < questionApi.lastIndex) {
                currentQuestionIndex.value++
                selectedAnswer.value = (-1)
            } else {
                quizJustEnd.value = true
            }
        }
    }
    LaunchedEffect(count) {
        while (count > 0) {
            delay(1000) // wait for 1 second
            count--
            if (count == 0) {
                quizAnsweredAction()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(elevation = 10.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.question) + "${currentQuestionIndex.value + 1}/${questionApi.size}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = if (count >= 0) {
                            "0:${count.toString().padStart(2, '0')} "
                        } else "",
                        fontWeight = FontWeight.Light,
                        color = DarkBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = questionApi[currentQuestionIndex.value].question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                questionApi[currentQuestionIndex.value].imageBitmap?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            questionApi[currentQuestionIndex.value].answerDtoSet.forEach { option ->
                Card(elevation = 2.dp) {
                    AnswerOption(
                        isClickable = count > 0,
                        option = option,
                        isSelected = option.id == selectedAnswer.value,
                        onOptionSelected = { selectedAnswer.value = it },
                        isAnswered = isAnswered.value,
                        isCorrectAnswer = option.id == questionApi[currentQuestionIndex.value].correctAnswer
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { quizAnsweredAction() },
            enabled = selectedAnswer.value != (-1) && !buttonLoadingState.value,
            modifier = Modifier.align(Alignment.End)
        ) {
            if (buttonLoadingState.value)
                BaseIndicator(size = 30.dp)
            else
                Text(text = stringResource(R.string.answer))
        }
    }
}

@Composable
fun AnswerOption(
    isClickable: Boolean,
    option: AnswerApi,
    isSelected: Boolean,
    onOptionSelected: (Int) -> Unit,
    isAnswered: Boolean,
    isCorrectAnswer: Boolean
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    val contentColor = if (isSelected) Color.White else MaterialTheme.colors.onSurface
    val backgroundCheckColor = when {
        (isCorrectAnswer) -> GreenSuccess
        (!isCorrectAnswer && isSelected) -> RedError
        else -> backgroundColor
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (isAnswered) backgroundCheckColor else backgroundColor)
            .clickable(onClick = { onOptionSelected(option.id) }, enabled = isClickable)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = option.answer,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}