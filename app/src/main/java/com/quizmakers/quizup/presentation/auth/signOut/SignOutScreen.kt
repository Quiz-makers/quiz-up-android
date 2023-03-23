package com.quizmakers.quizup.presentation.auth.signOut

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.quizup.ui.common.BaseButton
import com.quizmakers.quizup.ui.common.BaseTextField
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun SignOutScreen(
    navigator: DestinationsNavigator
) {
    SignOutScreen(
        navigateBack = navigator::popBackStack
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignOutScreen(
    navigateBack: () -> Unit
) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val surname = remember { mutableStateOf(TextFieldValue("")) }
    val userName = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSingOutScreen(navigateBack)
            Spacer(modifier = Modifier.height(30.dp))
            BaseTextField(
                valueState = name,
                labelText = "Wprowadź imię",
                placeholderText = "imię",
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = surname,
                labelText = "Wprowadź nazwisko",
                placeholderText = "nazwisko",
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = userName,
                labelText = "Wprowadź nick",
                placeholderText = "nick",
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = email,
                labelText = "Wprowadź email",
                placeholderText = "email",
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = password,
                labelText = "Wprowadź hasło",
                placeholderText = "hasło",
                focusManager = focusManager,
                isPassword = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() })


            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseButton(label = "Zarejestruj się", onClick = {
                keyboardController?.hide()
            })
        }
    }
}

@Composable
private fun HeaderSingOutScreen(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = Icons.Default.ArrowBack.name,
                tint = DarkBlue,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Zarejestruj się", fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
private fun SignOutScreenPreview() {
    QuizUpTheme {
        SignOutScreen(navigator = EmptyDestinationsNavigator)
    }
}