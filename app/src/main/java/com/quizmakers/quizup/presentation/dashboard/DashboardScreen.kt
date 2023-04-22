package com.quizmakers.quizup.presentation.dashboard


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizmakers.core.data.quizzes.local.QuizGeneralDisplayable
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.presentation.dashboard.DashboardScreenViewModel.DashboardState
import com.quizmakers.quizup.presentation.destinations.QuizDetailsBottomSheetDestination
import com.quizmakers.quizup.presentation.destinations.QuizManagerScreenDestination
import com.quizmakers.quizup.presentation.destinations.SignInScreenDestination
import com.quizmakers.quizup.ui.common.*
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.MediumBlue
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    dashboardScreenViewModel: DashboardScreenViewModel = koinViewModel(),
    snackbarHandler: SnackbarHandler
) {
    LaunchedEffect(Unit) {
        dashboardScreenViewModel.getQuizzes()
        dashboardScreenViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> {
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }
                BaseViewModel.MessageEvent.Success -> {
                    snackbarHandler.showSuccessSnackbar(R.string.add_quiz_success)
                }
            }
        }
    }
    DashboardScreen(
        navigateToSignInScreen = {
            dashboardScreenViewModel.logOutFromApp()
            navigator.navigateToSignInScreen()
        },
        navigateToQuizDetailsBottomSheet = { navigator.navigateToQuizDetailsBottomSheet(it) },
        navigateQuizManagerScreen = { navigator.navigateQuizManagerScreen() },
        onRefresh = dashboardScreenViewModel::getQuizzes,
        dashboardState = dashboardScreenViewModel.dashboardState.collectAsStateWithLifecycle().value,
        userName = dashboardScreenViewModel.getUserName(),
        getQuizByCode = dashboardScreenViewModel::getQuizByCode
    )
}

@Composable
private fun DashboardScreen(
    navigateToSignInScreen: () -> Unit,
    navigateToQuizDetailsBottomSheet: (QuizGeneralDisplayable) -> Unit,
    onRefresh: () -> Unit,
    dashboardState: DashboardState,
    navigateQuizManagerScreen: () -> Unit,
    userName: String,
    getQuizByCode: (String) -> Unit,
) {
    val boxSizePublic = remember { mutableStateOf(0) }
    val boxSizeUser = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MainAppBar(navigateToSignInScreen, userName)
            when (dashboardState) {
                is DashboardState.Error -> ErrorScreen(onClick = onRefresh)
                DashboardState.Loading -> LoadingScreen()
                DashboardState.None -> Unit
                is DashboardState.Success -> {
                    val publicQuizzes = dashboardState.data.first
                    val userQuizzes = dashboardState.data.second
                    val singleBoxPublic = if (publicQuizzes.size % 2 != 0) 125 else 0
                    val singleBoxUser = if (userQuizzes.size % 2 != 0) 125 else 0
                    boxSizePublic.value =
                        ((publicQuizzes.size / 2) * 100) + (publicQuizzes.size / 2) * 25 + singleBoxPublic
                    boxSizeUser.value =
                        ((userQuizzes.size / 2) * 100) + (userQuizzes.size / 2) * 25 + singleBoxUser
                    DashboardData(
                        navigateQuizManagerScreen = navigateQuizManagerScreen,
                        publicQuizzes = publicQuizzes,
                        userQuizzes = userQuizzes,
                        boxSizePublic = boxSizePublic,
                        boxSizeUser = boxSizeUser,
                        navigateToQuizDetailsBottomSheet = navigateToQuizDetailsBottomSheet,
                        getQuizByCode = getQuizByCode
                    )
                }
            }

        }

    }
}

@Composable
private fun DashboardData(
    publicQuizzes: List<QuizGeneralDisplayable>,
    userQuizzes: List<QuizGeneralDisplayable>,
    boxSizePublic: MutableState<Int>,
    boxSizeUser: MutableState<Int>,
    navigateToQuizDetailsBottomSheet: (QuizGeneralDisplayable) -> Unit,
    navigateQuizManagerScreen: () -> Unit,
    getQuizByCode: (String) -> Unit,
) {

    val search = remember { mutableStateOf(TextFieldValue("")) }
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            CodeCard(search, getQuizByCode)
            Spacer(modifier = Modifier.height(8.dp))
            if (publicQuizzes.isNotEmpty())
                Text(
                    text = stringResource(R.string.latest_quizzes),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            Spacer(modifier = Modifier.height(8.dp))
            QuizzesList(
                cardData = publicQuizzes,
                boxSize = boxSizePublic.value,
                onClick = navigateToQuizDetailsBottomSheet
            )
            if (userQuizzes.isNotEmpty())
                Text(
                    text = stringResource(R.string.my_quiz),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            Spacer(modifier = Modifier.height(8.dp))
            QuizzesList(
                cardData = userQuizzes,
                boxSize = boxSizeUser.value,
                onClick = navigateToQuizDetailsBottomSheet
            )
            Spacer(modifier = Modifier.height(8.dp))
            BaseButtonWithIcon(
                label = stringResource(R.string.add_new_quiz),
                icon = Icons.Default.Add,
                onClick = navigateQuizManagerScreen,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun MainAppBar(navigateToSignInScreen: () -> Unit, userName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(R.string.hi))
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(MediumBlue, DarkBlue)
                        )
                    )
                ) {
                    append("$userName 👋 ")
                }
            }
        )
        IconButton(
            onClick = navigateToSignInScreen,
            modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = Icons.Default.ExitToApp.name,
                tint = DarkBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun QuizzesList(
    cardData: List<QuizGeneralDisplayable>, boxSize: Int,
    onClick: (QuizGeneralDisplayable) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.height(boxSize.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(5.dp),
    ) {
        cardData.forEach { card ->
            item(span = { GridItemSpan(1) }) {
                QuizCardItem(title = card.title, onClick = { onClick(card) })
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CodeCard(search: MutableState<TextFieldValue>, getQuizByCode: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Card(elevation = 10.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.code_card_info))
            Spacer(modifier = Modifier.height(8.dp))
            BaseTextFieldWithIcon(
                valueState = search,
                trailingIcon = Icons.Default.Add,
                placeholderText = stringResource(R.string.code),
                labelText = stringResource(R.string.enter_code_info),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        getQuizByCode(search.value.text)
                    }
                )
            )
        }
    }
}

@Composable
fun QuizCardItem(
    title: String,
    onClick: () -> Unit,
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .clickable { onClick.invoke() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ask_ic),
                contentDescription = Icons.Default.Face.name,
                tint = Color.Black,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun DestinationsNavigator.navigateToSignInScreen() {
    navigate(SignInScreenDestination()) {
        popBackStack()
    }
}

private fun DestinationsNavigator.navigateToQuizDetailsBottomSheet(quiz: QuizGeneralDisplayable) {
    navigate(
        QuizDetailsBottomSheetDestination(
            quiz.quizId.toString(),
            quiz.description
        )
    )
}


private fun DestinationsNavigator.navigateQuizManagerScreen() {
    navigate(QuizManagerScreenDestination())
}

@Preview(
    showBackground = true
)
@Composable
private fun DashboardScreenPreview() {
    QuizUpTheme {
        DashboardScreen({}, {}, {}, DashboardState.None, { }, "", {})
    }
}