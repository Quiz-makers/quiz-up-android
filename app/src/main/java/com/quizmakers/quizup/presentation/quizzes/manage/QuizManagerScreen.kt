package com.quizmakers.quizup.presentation.quizzes.manage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.quizup.R
import com.quizmakers.quizup.ui.common.BaseButtonWithIcon
import com.quizmakers.quizup.ui.common.BaseTextFieldRounded
import com.quizmakers.quizup.ui.common.BaseToggleButton
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.LightBlue
import com.quizmakers.quizup.ui.theme.MediumBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun QuizManagerScreen(
    navigator: DestinationsNavigator
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 0.dp, end = 20.dp, start = 20.dp),
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
        AddQuizScreen()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddQuizScreen() {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val categories = listOf("History", "Science", "Geography", "Sports", "Movies")

    val quizTitle = remember { mutableStateOf(TextFieldValue("")) }
    val quizDescription = remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory = remember { mutableStateOf("") }
    val isPrivateQuiz = remember { mutableStateOf(false) }
    var expanded = remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            BaseTextFieldRounded(
                valueState = quizTitle,
                placeholderText = "Tytul",
                labelText = "Wprowadź tytul",
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Home
            )
            BaseTextFieldRounded(
                valueState = quizDescription,
                placeholderText = "Opis",
                labelText = "Wprowadź Opis",
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
                    text = if (selectedCategory.value.isNotBlank()) selectedCategory.value else "Dodaj kategorię",
                    color = if (selectedCategory.value.isNotBlank()) Color.Black else Color.Gray,
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
                categories.forEach { category ->
                    DropdownMenuItem(onClick = {
                        selectedCategory.value = category
                        expanded.value = false
                    }) {
                        Text(category, fontSize = 12.sp)
                    }
                }
            }
            AddQuizQuestions()
            Spacer(modifier = Modifier.height(8.dp))
            BaseButtonWithIcon(
                label = "Dodaj", icon = Icons.Default.Add, onClick = {}, modifier = Modifier
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

@Composable
fun AddQuizQuestions() {
    val questions = remember { mutableStateListOf("") }
    val answers = remember { mutableStateListOf(listOf("", "", "", "")) }
    val correctAnswers = remember { mutableStateListOf(0, 0, 0, 0) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // list of quiz questions
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(questions.size) { index ->
                QuestionItem(question = questions[index],
                    answers = answers[index],
                    correctAnswer = correctAnswers[index],
                    onQuestionChange = { questions[index] = it },
                    onAnswerChange = { answers[index] = it },
                    onCorrectAnswerChange = { correctAnswers[index] = it })
            }

            // Button to add new question
            item {
                IconButton(
                    onClick = {
                        questions.add("")
                        answers.add(listOf("", "", "", ""))
                        correctAnswers.add(0)
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = MediumBlue)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add question",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: String,
    answers: List<String>,
    correctAnswer: Int,
    onQuestionChange: (String) -> Unit,
    onAnswerChange: (List<String>) -> Unit,
    onCorrectAnswerChange: (Int) -> Unit
) {

    // question
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(value = question,
            onValueChange = onQuestionChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Pytanie", fontSize = 14.sp) },
            label = { Text(text = "Wprowadż pytanie", fontSize = 14.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = Icons.Default.Edit.name,
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            })
        Spacer(modifier = Modifier.height(8.dp))

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
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
    ) {
        // answer text field
        OutlinedTextField(
            value = answer,
            onValueChange = onAnswerChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            placeholder = { Text(text = "Odpowiedź", fontSize = 14.sp) },
            label = { Text(text = "Wprowadż odpowiedź", fontSize = 14.sp) },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = onCorrectAnswerChange,
                    modifier = Modifier.clip(CircleShape)
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