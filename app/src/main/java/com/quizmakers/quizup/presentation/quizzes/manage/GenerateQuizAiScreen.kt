package com.quizmakers.quizup.presentation.quizzes.manage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.ui.common.BaseButtonWithIcon
import com.quizmakers.quizup.ui.common.BaseIndicator
import com.quizmakers.quizup.ui.common.BaseTextFieldRounded
import com.quizmakers.quizup.ui.common.BaseToggleButton
import com.quizmakers.quizup.ui.common.ErrorScreen
import com.quizmakers.quizup.ui.common.LoadingScreen
import com.quizmakers.quizup.ui.common.SnackbarHandler
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun GenerateQuizAiScreen(
    snackbarHandler: SnackbarHandler,
    generateQuizViewModel: GenerateQuizViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(Unit) {
        generateQuizViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> {
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }

                BaseViewModel.MessageEvent.Success -> {
                    snackbarHandler.showSuccessSnackbar(R.string.add_quiz_success)
                    navigator.navigateUp()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 0.dp, end = 10.dp, start = 10.dp),
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
        GenerateQuizAiScreen(
            generateQuiz = generateQuizViewModel::generateQuiz,
            getCategories = generateQuizViewModel::getCategories,
            quizManagerState = generateQuizViewModel.quizManagerState.collectAsStateWithLifecycle().value,
            addedState = generateQuizViewModel.addedState.collectAsStateWithLifecycle().value,
        )
    }
}

@Composable
private fun GenerateQuizAiScreen(
    getCategories: () -> Unit,
    generateQuiz: (
        title: String,
        numberOfQuestions: Int,
        answersPerQuestion: Int,
        selectedCategoryId: Int,
        publicAvailable: Boolean
    ) -> Unit,
    quizManagerState: GenerateQuizViewModel.QuizAiState,
    addedState: GenerateQuizViewModel.AddQuizState
) {
    when (quizManagerState) {
        is GenerateQuizViewModel.QuizAiState.Error -> ErrorScreen { getCategories.invoke() }
        GenerateQuizViewModel.QuizAiState.Loading -> LoadingScreen()
        GenerateQuizViewModel.QuizAiState.None -> Unit
        is GenerateQuizViewModel.QuizAiState.Success -> AddQuizLayout(
            categories = quizManagerState.categories,
            generateQuiz = generateQuiz,
            addedState = addedState
        )
    }
}

@Composable
private fun AddQuizLayout(
    categories: List<CategoryApi>,
    generateQuiz: (
        title: String,
        numberOfQuestions: Int,
        answersPerQuestion: Int,
        selectedCategoryId: Int,
        publicAvailable: Boolean
    ) -> Unit,
    addedState: GenerateQuizViewModel.AddQuizState
) {
    val focusManager = LocalFocusManager.current

    val quizTitle = remember { mutableStateOf(TextFieldValue("")) }

    val isPrivateQuiz = remember { mutableStateOf(false) }

    val numberOfQuestions = remember { mutableStateOf(2) }
    val answersPerQuestion = remember { mutableStateOf(4) }

    val expanded = remember { mutableStateOf(false) }
    val selectedCategory = remember { mutableStateOf(Pair(0, "")) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                BaseTextFieldRounded(
                    valueState = quizTitle,
                    placeholderText = stringResource(R.string.quiz_title),
                    labelText = stringResource(R.string.enter_quiz_desc),
                    focusManager = focusManager,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = Icons.Default.Home
                )
                Spacer(modifier = Modifier.height(4.dp))
                BaseToggleButton(checked = isPrivateQuiz.value, onCheckedChange = {
                    isPrivateQuiz.value = it
                })
                Spacer(modifier = Modifier.height(4.dp))
                SpinnerCategory(selectedCategory, expanded, categories)
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = stringResource(R.string.wybierz_liczb_pyta))
                HorizontalNumberPicker(
                    min = 1,
                    max = 20,
                    default = numberOfQuestions.value,
                    onValueChange = { value ->
                        numberOfQuestions.value = value
                    })
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = stringResource(R.string.numer_of_anwer))
                HorizontalNumberPicker(
                    min = 2,
                    max = 5,
                    default = answersPerQuestion.value,
                    onValueChange = { value ->
                        numberOfQuestions.value = value
                    })
                Spacer(modifier = Modifier.height(20.dp))
                when (addedState) {
                    GenerateQuizViewModel.AddQuizState.Loaded -> BaseButtonWithIcon(
                        label = stringResource(R.string.generate_quiz),
                        icon = Icons.Default.Add,
                        onClick = {
                            generateQuiz(
                                quizTitle.value.text,
                                numberOfQuestions.value,
                                answersPerQuestion.value,
                                selectedCategory.value.first,
                                isPrivateQuiz.value
                            )
                        },
                        modifier = Modifier
                    )

                    GenerateQuizViewModel.AddQuizState.Loading -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BaseIndicator(size = 30.dp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

        }
        Image(
            painter = painterResource(id = R.drawable.gpt),
            contentDescription = Icons.Default.Close.name,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomEnd)
        )
    }
}
