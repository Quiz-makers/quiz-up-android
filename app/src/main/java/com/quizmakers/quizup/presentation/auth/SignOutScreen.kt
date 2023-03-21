package com.quizmakers.quizup.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun SignOutScreen(
    navigator: DestinationsNavigator
) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val surname = remember { mutableStateOf(TextFieldValue("")) }
    val userName = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navigator.popBackStack() },
                    modifier = Modifier
                        .then(Modifier.size(40.dp))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "content description",
                        tint = Color.White
                    )
                }
                Text(text = "Sign out,", fontSize = 34.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedTextField(
                value = name.value,
                onValueChange = { newText ->
                    name.value = newText
                },
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Wprowadź imię") },
                placeholder = { Text(text = "imię") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = surname.value,
                onValueChange = { newText ->
                    surname.value = newText
                },
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Wprowadź nazwisko") },
                placeholder = { Text(text = "nazwisko") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = userName.value,
                onValueChange = { newText ->
                    userName.value = newText
                },
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Wprowadź nick") },
                placeholder = { Text(text = "nick") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email.value,
                onValueChange = { newText ->
                    email.value = newText
                },
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Wprowadź email") },
                placeholder = { Text(text = "email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { newText ->
                    password.value = newText
                },
                singleLine = true,
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Wprowadź hasło") },
                placeholder = { Text(text = "hasło") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Red, Color.Black
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                ),
                onClick = { },
                elevation = ButtonDefaults.elevation(0.dp, 0.dp)
            ) {
                Text(text = "Zarejestruj się", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun SignOutScreenPreview() {
    QuizUpTheme {
        SignOutScreen(navigator = EmptyDestinationsNavigator)
    }
}