package com.quizmakers.quizup.presentation.auth.signIn

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizmakers.quizup.presentation.auth.destinations.SignOutScreenDestination
import com.quizmakers.quizup.ui.common.BaseButton
import com.quizmakers.quizup.ui.common.BaseIndicator
import com.quizmakers.quizup.ui.common.BaseTextField
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
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        signInViewModel.authEvent.collect {
            when (it) {
                is SignInViewModel.AuthEvent.Error -> mToast(context, it.error)
                SignInViewModel.AuthEvent.Success -> mToast(context, "Success")
            }
        }
    }
    when (val authState = signInViewModel.authState.collectAsStateWithLifecycle().value) {
        SignInViewModel.AuthState.Loading -> LoadingScreen()
        is SignInViewModel.AuthState.Error -> {
            SignInScreen(
                authState = authState,
                navigateToSignUpScreen = navigator::navigateToSignOutScreen,
                signInViewModel = signInViewModel::signIn
            )
        }
        SignInViewModel.AuthState.None -> {
            SignInScreen(
                authState = authState,
                navigateToSignUpScreen = navigator::navigateToSignOutScreen,
                signInViewModel = signInViewModel::signIn
            )
        }
    }

}

// Function to generate a Toast
private fun mToast(context: Context, text: String) {
    Toast.makeText(context, "This is a Sample Toast $text", Toast.LENGTH_LONG).show()
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BaseIndicator()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignInScreen(
    navigateToSignUpScreen: () -> Unit,
    signInViewModel: (String, String) -> Unit,
    authState: SignInViewModel.AuthState,

    ) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                    onDone = {
                        signInViewModel.invoke(email.value.text, password.value.text)
                        keyboardController?.hide()
                    })
            )
            Spacer(modifier = Modifier.weight(0.1f))
            BaseButton(
                label = "Zaloguj",
                onClick = {
                    signInViewModel.invoke(email.value.text, password.value.text)
                    keyboardController?.hide()
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.weight(0.4f))
            FooterSignInScreen(onClick = navigateToSignUpScreen)
        }
    }
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
        Text(text = "Jestem nowym użytkownikiem.", fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Zarejestruj się",
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
             authState = SignInViewModel.AuthState.None
        )
    }
}