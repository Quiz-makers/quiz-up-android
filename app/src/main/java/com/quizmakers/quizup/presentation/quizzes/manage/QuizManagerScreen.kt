package com.quizmakers.quizup.presentation.quizzes.manage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.core.data.quizzes.remote.QuestionsRequestApi
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.ui.common.*
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.LightBlue
import com.quizmakers.quizup.ui.theme.MediumBlue
import com.quizmakers.quizup.utils.encodeImage
import com.quizmakers.quizup.utils.getBitmapFromUri
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun QuizManagerScreen(
    quizManagerViewModel: QuizManagerViewModel = koinViewModel(),
    snackbarHandler: SnackbarHandler,
    navigator: DestinationsNavigator
) {
    LaunchedEffect(Unit) {
        quizManagerViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> {
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }
                BaseViewModel.MessageEvent.Success -> {
                    snackbarHandler.showSuccessSnackbar(message = "Dodano quiz ! 🔮")
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
        QuizManagerScreen(
            addNewQuiz = quizManagerViewModel::addNewQuiz,
            getCategories = quizManagerViewModel::getCategories,
            quizManagerState = quizManagerViewModel.quizManagerState.collectAsStateWithLifecycle().value,
            addedState = quizManagerViewModel.addedState.collectAsStateWithLifecycle().value,
        )
    }
}

@Composable
private fun QuizManagerScreen(
    getCategories: () -> Unit,
    addNewQuiz: (String, String, Boolean, Int, QuestionsRequestApi) -> Unit,
    quizManagerState: QuizManagerViewModel.QuizManagerState,
    addedState: QuizManagerViewModel.AddQuizState
) {
    when (quizManagerState) {
        is QuizManagerViewModel.QuizManagerState.Error -> ErrorScreen { getCategories.invoke() }
        QuizManagerViewModel.QuizManagerState.Loading -> LoadingScreen()
        QuizManagerViewModel.QuizManagerState.None -> Unit
        is QuizManagerViewModel.QuizManagerState.Success -> AddQuizLayout(
            categories = quizManagerState.categories,
            addNewQuiz = addNewQuiz,
            addedState = addedState
        )
    }

}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun AddQuizLayout(
    categories: List<CategoryApi>,
    addNewQuiz: (String, String, Boolean, Int, QuestionsRequestApi) -> Unit,
    addedState: QuizManagerViewModel.AddQuizState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val quizTitle = remember { mutableStateOf(TextFieldValue("")) }
    val quizDescription = remember { mutableStateOf(TextFieldValue("")) }

    val isPrivateQuiz = remember { mutableStateOf(false) }

    val expanded = remember { mutableStateOf(false) }
    val selectedCategory = remember { mutableStateOf(Pair(0, "")) }

    val questions = remember { mutableStateListOf("") }
    val image = remember { mutableStateListOf<Uri?>(null) }
    val answers = remember { mutableStateListOf(listOf("", "", "", "")) }
    val correctAnswers = remember { mutableStateListOf(0) }

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
            BaseTextFieldRounded(
                valueState = quizDescription,
                placeholderText = stringResource(R.string.description),
                labelText = stringResource(R.string.enter_desc),
                focusManager = focusManager,
                singleLine = false,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                leadingIcon = Icons.Default.Info
            )
            Spacer(modifier = Modifier.height(4.dp))
            BaseToggleButton(checked = isPrivateQuiz.value, onCheckedChange = {
                isPrivateQuiz.value = it
            })
            Spacer(modifier = Modifier.height(4.dp))
            SpinnerCategory(selectedCategory, expanded, categories)
            Spacer(modifier = Modifier.height(4.dp))
            AddQuizQuestions(questions, image, answers, correctAnswers)
            Spacer(modifier = Modifier.height(4.dp))
            when (addedState) {
                QuizManagerViewModel.AddQuizState.Loaded -> BaseButtonWithIcon(
                    label = stringResource(R.string.add),
                    icon = Icons.Default.Add,
                    onClick = {
                        addNewQuiz(
                            quizTitle.value.text,
                            quizDescription.value.text,
                            isPrivateQuiz.value,
                            selectedCategory.value.first,
                            QuestionsRequestApi(
                                questions.toList(),
                                image.toList().map { uri ->
                                    uri?.let {
                                        context.getBitmapFromUri(it).encodeImage()
                                    }
                                },
                                answers.toList(),
                                correctAnswers.toList()
                            )
                        )
                    },
                    modifier = Modifier
                )
                QuizManagerViewModel.AddQuizState.Loading -> Row(
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
}


@Composable
private fun SpinnerCategory(
    selectedCategory: MutableState<Pair<Int, String>>,
    expanded: MutableState<Boolean>,
    categories: List<CategoryApi>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(10.dp)
            )
            .background(
                shape = RoundedCornerShape(10.dp), color = LightBlue
            )
            .padding(10.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (selectedCategory.value.second.isNotBlank()) selectedCategory.value.second else stringResource(
                R.string.add_category
            ),
            color = if (selectedCategory.value.second.isNotBlank()) Color.Black else Color.Gray,
            fontSize = 14.sp
        )
        IconButton(
            onClick = { expanded.value = true }, modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = Icons.Default.ArrowDropDown.name,
                tint = DarkBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { value ->
            DropdownMenuItem(onClick = {
                selectedCategory.value = Pair(value.categoryId, value.category)
                expanded.value = false
            }) {
                Text(value.category, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AddQuizQuestions(
    questions: SnapshotStateList<String>,
    image: SnapshotStateList<Uri?>,
    answers: SnapshotStateList<List<String>>,
    correctAnswers: SnapshotStateList<Int>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
            verticalAlignment = Alignment.Top
        ) {
            items(questions.size) { index ->
                QuestionItem(question = questions[index],
                    position = index + 1,
                    answers = answers[index],
                    imageUri = image[index],
                    correctAnswer = correctAnswers[index],
                    onQuestionChange = { questions[index] = it },
                    onAnswerChange = { answers[index] = it },
                    onCorrectAnswerChange = { correctAnswers[index] = it },
                    onDeleteItem = {
                        image.removeAt(index)
                        questions.removeAt(index)
                        answers.removeAt(index)
                        correctAnswers.removeAt(index)
                    },
                    onImageAdd = { image[index] = it }
                )
            }
        }
        NewQuestionAction(
            questions = questions, image = image, answers = answers, correctAnswers = correctAnswers
        )
    }
}

@Composable
private fun NewQuestionAction(
    questions: SnapshotStateList<String>,
    answers: SnapshotStateList<List<String>>,
    correctAnswers: SnapshotStateList<Int>,
    image: SnapshotStateList<Uri?>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.add_new_question), fontSize = 12.sp, color = Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = {
                questions.add("")
                image.add(null)
                answers.add(listOf("", "", "", ""))
                correctAnswers.add(0)
            }, modifier = Modifier
                .clip(CircleShape)
                .background(color = MediumBlue)
                .size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = Icons.Outlined.Add.name,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun QuestionItem(
    position: Int,
    question: String,
    answers: List<String>,
    imageUri: Uri?,
    correctAnswer: Int,
    onQuestionChange: (String) -> Unit,
    onAnswerChange: (List<String>) -> Unit,
    onCorrectAnswerChange: (Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onImageAdd: (Uri) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = question,
                onValueChange = onQuestionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.question), fontSize = 14.sp
                    )
                },
                label = { Text(text = stringResource(R.string.enter_question), fontSize = 14.sp) },
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                leadingIcon = {
                    Text(
                        text = position.toString(),
                        fontSize = 20.sp,
                        color = MediumBlue,
                        fontWeight = FontWeight.Bold,
                    )
                },
                trailingIcon = {
                    if (position != 1) IconButton(
                        onClick = { onDeleteItem(position - 1) },
                        modifier = Modifier.clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = Icons.Outlined.Delete.name,
                            tint = DarkBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                })
        }
        Spacer(modifier = Modifier.height(4.dp))
        // answers
        answers.forEachIndexed { index, answer ->
            AnswerItem(answer = answer,
                isCorrect = index == correctAnswer,
                onAnswerChange = { newAnswer ->
                    val updatedAnswers = answers.toMutableList()
                    updatedAnswers[index] = newAnswer
                    onAnswerChange(updatedAnswers)
                },
                onCorrectAnswerChange = { onCorrectAnswerChange(index) })
        }
        //Add Image 
        var selectImage by remember { mutableStateOf<Uri?>(imageUri) }
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                selectImage = it
                it?.let {
                    onImageAdd(it)
                }
            }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            selectImage?.let {
                Image(painter = rememberAsyncImagePainter(it),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .size(120.dp)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        })
            } ?: IconButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.size(120.dp)
            ) {
                Icon(
                    painter = rememberAsyncImagePainter(model = R.drawable.add_image_ic),
                    contentDescription = Icons.Default.MoreVert.name,
                    tint = DarkBlue,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun AnswerItem(
    answer: String,
    isCorrect: Boolean,
    onAnswerChange: (String) -> Unit,
    onCorrectAnswerChange: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(value = answer,
            onValueChange = onAnswerChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            placeholder = { Text(text = stringResource(id = R.string.answer), fontSize = 14.sp) },
            label = { Text(text = stringResource(R.string.enter_answer), fontSize = 14.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = onCorrectAnswerChange, modifier = Modifier.clip(CircleShape)
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.CheckCircle,
                        contentDescription = if (isCorrect) Icons.Default.Check.name else Icons.Default.CheckCircle.name,
                        tint = DarkBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            })
    }
}