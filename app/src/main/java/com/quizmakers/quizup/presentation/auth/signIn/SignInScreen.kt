package com.quizmakers.quizup.presentation.auth.signIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.presentation.auth.destinations.SignOutScreenDestination
import com.quizmakers.quizup.ui.common.*
import com.quizmakers.quizup.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun SignInScreen(
    navigator: DestinationsNavigator,
    signInViewModel: SignInViewModel = koinViewModel(),
    snackbarHandler: SnackbarHandler,
) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        signInViewModel.authEvent.collect {
            when (it) {
                is BaseViewModel.AuthEvent.Error -> {
                    isError.value = true
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }
                BaseViewModel.AuthEvent.Success -> {
                    isError.value = false
                    //navToDashboard
                }
            }
        }
    }
    when (val authState = signInViewModel.authState.collectAsStateWithLifecycle().value) {
        SignInViewModel.AuthState.Loading -> LoadingScreen()
        else -> {
            SignInScreen(
                navigateToSignUpScreen = navigator::navigateToSignOutScreen,
                signIn = signInViewModel::signIn,
                authState = authState,
                email = email,
                password = password,
                isError = isError
            )
        }
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignInScreen(
    navigateToSignUpScreen: () -> Unit,
    signIn: (String, String) -> Unit,
    authState: SignInViewModel.AuthState,
    email: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    isError: MutableState<Boolean>

) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val emailErrorMessage = remember { mutableStateOf("") }
    val passwordErrorMessage = remember { mutableStateOf("") }

    validateError(
        authState,
        emailErrorMessage,
        passwordErrorMessage
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 40.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSignInScreen()
            Spacer(modifier = Modifier.height(60.dp))
            BaseTextField(
                valueState = email,
                labelText = stringResource(id = R.string.enter_email),
                placeholderText = stringResource(id = R.string.email),
                isError = isError.value,
                onResetError = {
                    isError.value = false
                    emailErrorMessage.value = ""
                },
                errorMessage = emailErrorMessage.value,
                focusManager = focusManager,
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = password,
                labelText = stringResource(id = R.string.enter_your_password),
                placeholderText = stringResource(id = R.string.password),
                focusManager = focusManager,
                isError = isError.value,
                isPassword = true,
                errorMessage = passwordErrorMessage.value,
                onResetError = {
                    isError.value = false
                    passwordErrorMessage.value = ""
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        signIn(signIn, email, password, keyboardController)
                    })
            )
            Spacer(modifier = Modifier.weight(0.1f))
            BaseButton(
                label = stringResource(R.string.log_in),
                onClick = {
                    signIn(signIn, email, password, keyboardController)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.weight(0.4f))
            FooterSignInScreen(onClick = navigateToSignUpScreen)
        }
    }
}

@Composable
private fun validateError(
    authState: SignInViewModel.AuthState,
    emailErrorMessage: MutableState<String>,
    passwordErrorMessage: MutableState<String>
) {
    when (authState) {
        is SignInViewModel.AuthState.Error -> {
            emailErrorMessage.value =
                authState.errorField.find { it.signInField == SignInViewModel.SignInFieldInfo.EMAIL }?.error
                    ?: ""
            passwordErrorMessage.value =
                authState.errorField.find { it.signInField == SignInViewModel.SignInFieldInfo.PASSWORD }?.error
                    ?: ""
        }
        else -> Unit
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun signIn(
    signInViewModel: (String, String) -> Unit,
    email: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    keyboardController: SoftwareKeyboardController?
) {
    signInViewModel.invoke(email.value.text, password.value.text)
    keyboardController?.hide()
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun FooterSignInScreen(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.i_am_a_new_user), fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.register),
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(MediumBlue, DarkBlue)
                )
            ),
            modifier = Modifier.clickable(onClick = onClick),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}


@Composable
@OptIn(ExperimentalTextApi::class)
private fun HeaderSignInScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Witaj w \n")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Quiz")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 45.sp,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Cyan, DarkBlue)
                        )
                    )
                ) {
                    append("UP")
                }

            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Zaloguj się by kontynuować!",
            fontSize = 20.sp,
        )
    }
}


private fun DestinationsNavigator.navigateToSignOutScreen() {
    navigate(SignOutScreenDestination())
}

@Preview(
    showBackground = true
)
@Composable
private fun SignInScreenPreview() {
    QuizUpTheme {
        SignInScreen(
            navigateToSignUpScreen = {},
            { _, _ -> },
            authState = SignInViewModel.AuthState.None,
            email = remember { mutableStateOf(TextFieldValue("")) },
            password = remember { mutableStateOf(TextFieldValue("")) },
            isError = remember { mutableStateOf(true) }
        )
    }
}