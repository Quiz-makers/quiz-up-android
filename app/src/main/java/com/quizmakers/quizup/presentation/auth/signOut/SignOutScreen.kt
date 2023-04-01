package com.quizmakers.quizup.presentation.auth.signOut

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import com.quizmakers.quizup.presentation.destinations.DashboardScreenDestination
import com.quizmakers.quizup.ui.common.BaseButton
import com.quizmakers.quizup.ui.common.BaseTextField
import com.quizmakers.quizup.ui.common.LoadingScreen
import com.quizmakers.quizup.ui.common.SnackbarHandler
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.QuizUpTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SignOutScreen(
    navigator: DestinationsNavigator,
    signOutViewModel: SignOutViewModel = koinViewModel(),
    snackbarHandler: SnackbarHandler,
) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val surname = remember { mutableStateOf(TextFieldValue("")) }
    val userName = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        signOutViewModel.authEvent.collect {
            when (it) {
                is BaseViewModel.AuthEvent.Error -> {
                    snackbarHandler.showErrorSnackbar(message = it.error)
                }
                BaseViewModel.AuthEvent.Success -> {
                    navigator.navigateToDashboardScreen()
                }
            }
        }
    }
    when (val authState = signOutViewModel.authState.collectAsStateWithLifecycle().value) {
        SignOutViewModel.AuthState.Loading -> LoadingScreen()
        else -> {
            SignOutScreen(
                authState = authState,
                name = name,
                surname = surname,
                userName = userName,
                email = email,
                password = password,
                navigateBack = navigator::popBackStack,
                signOut = signOutViewModel::signOut,
            )
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignOutScreen(
    navigateBack: () -> Unit,
    name: MutableState<TextFieldValue>,
    surname: MutableState<TextFieldValue>,
    userName: MutableState<TextFieldValue>,
    email: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    authState: SignOutViewModel.AuthState,
    signOut: (String, String, String, String, String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val nameErrorMessage = remember { mutableStateOf("") }
    val surnameErrorMessage = remember { mutableStateOf("") }
    val usernameErrorMessage = remember { mutableStateOf("") }
    val emailErrorMessage = remember { mutableStateOf("") }
    val passwordErrorMessage = remember { mutableStateOf("") }

    ValidateError(
        authState,
        nameErrorMessage,
        surnameErrorMessage,
        usernameErrorMessage,
        emailErrorMessage,
        passwordErrorMessage
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            HeaderSingOutScreen(navigateBack)
            Spacer(modifier = Modifier.height(30.dp))
            BaseTextField(
                valueState = name,
                onResetError = { nameErrorMessage.value = "" },
                errorMessage = nameErrorMessage.value,
                labelText = stringResource(R.string.enter_name),
                placeholderText = stringResource(R.string.name),
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = surname,
                onResetError = { surnameErrorMessage.value = "" },
                errorMessage = surnameErrorMessage.value,
                labelText = stringResource(R.string.enter_last_name),
                placeholderText = stringResource(R.string.surname),
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = userName,
                onResetError = { usernameErrorMessage.value = "" },
                errorMessage = usernameErrorMessage.value,
                labelText = stringResource(R.string.enter_user_name),
                placeholderText = stringResource(R.string.user_name),
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = email,
                onResetError = { emailErrorMessage.value = "" },
                errorMessage = emailErrorMessage.value,
                labelText = stringResource(R.string.enter_email),
                placeholderText = stringResource(R.string.email),
                focusManager = focusManager
            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseTextField(
                valueState = password,
                onResetError = { passwordErrorMessage.value = "" },
                errorMessage = passwordErrorMessage.value,
                labelText = stringResource(R.string.enter_your_password),
                placeholderText = stringResource(R.string.password),
                focusManager = focusManager,
                isPassword = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        signOut(
                            signOut,
                            name,
                            surname,
                            userName,
                            email,
                            password,
                            keyboardController
                        )
                    })


            )
            Spacer(modifier = Modifier.height(16.dp))
            BaseButton(label = stringResource(id = R.string.register), onClick = {
                signOut.invoke(
                    name.value.text,
                    surname.value.text,
                    userName.value.text,
                    email.value.text,
                    password.value.text
                )
                keyboardController?.hide()
            })
        }
    }
}

private fun DestinationsNavigator.navigateToDashboardScreen() {
    navigate(DashboardScreenDestination()) {
        popBackStack()
    }
}

@Composable
private fun ValidateError(
    authState: SignOutViewModel.AuthState,
    nameErrorMessage: MutableState<String>,
    surnameErrorMessage: MutableState<String>,
    usernameErrorMessage: MutableState<String>,
    emailErrorMessage: MutableState<String>,
    passwordErrorMessage: MutableState<String>
) {
    when (authState) {
        is SignOutViewModel.AuthState.Error -> {
            nameErrorMessage.value =
                authState.errorField.find { it.signInField == SignOutViewModel.SignOutFieldInfo.NAME }?.error
                    ?: ""
            surnameErrorMessage.value =
                authState.errorField.find { it.signInField == SignOutViewModel.SignOutFieldInfo.SURNAME }?.error
                    ?: ""
            usernameErrorMessage.value =
                authState.errorField.find { it.signInField == SignOutViewModel.SignOutFieldInfo.USERNAME }?.error
                    ?: ""
            emailErrorMessage.value =
                authState.errorField.find { it.signInField == SignOutViewModel.SignOutFieldInfo.EMAIL }?.error
                    ?: ""
            passwordErrorMessage.value =
                authState.errorField.find { it.signInField == SignOutViewModel.SignOutFieldInfo.PASSWORD }?.error
                    ?: ""
        }
        else -> Unit
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun signOut(
    signOut: (String, String, String, String, String) -> Unit,
    name: MutableState<TextFieldValue>,
    surname: MutableState<TextFieldValue>,
    userName: MutableState<TextFieldValue>,
    email: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    keyboardController: SoftwareKeyboardController?
) {
    signOut.invoke(
        name.value.text,
        surname.value.text,
        userName.value.text,
        email.value.text,
        password.value.text
    )
    keyboardController?.hide()
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
        Text(
            text = stringResource(R.string.register),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.signout_screen_subtitle),
            fontSize = 20.sp,
        )
    }
}

@Preview
@Composable
private fun SignOutScreenPreview() {
    QuizUpTheme {
    }
}