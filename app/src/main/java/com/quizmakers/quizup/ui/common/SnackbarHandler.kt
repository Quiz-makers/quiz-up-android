package com.quizmakers.quizup.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.GreenSuccess
import com.quizmakers.quizup.ui.theme.RedError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

private val BOTTOM_BAR_HEIGHT = 56.dp

@Composable
fun QuizUpSnackbar(
    modifier: Modifier = Modifier, navController: NavController, snackbarHandler: SnackbarHandler
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isVisible by remember { mutableStateOf(false) }
    var isNavBarVisible by remember { mutableStateOf(false) }
    var isSuccessSnackbar by remember { mutableStateOf(false) }

    val snackbarPadding = if (isNavBarVisible) BOTTOM_BAR_HEIGHT else 0.dp
    val currentSnackbarData = snackbarHostState.currentSnackbarData
    val systemUiController = rememberSystemUiController()
    val snackbarColor = if (isSuccessSnackbar) GreenSuccess else RedError
    val destinationChangedListener = OnDestinationChangedListener { _, _, _ ->
        systemUiController.setNavigationBarColor(White)
        snackbarHostState.currentSnackbarData?.dismiss()
        isVisible = false
    }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        navController.addOnDestinationChangedListener(destinationChangedListener)
        onDispose {
            navController.removeOnDestinationChangedListener(destinationChangedListener)
        }
    }

    LaunchedEffect(Unit) {
        snackbarHandler.showSnackBarEvent.collect { data ->
            isSuccessSnackbar = data.success
            val message = data.message ?: data.messageResId?.let { context.getString(it) }
            message?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(currentSnackbarData) {
        currentSnackbarData?.let { snackbarData ->
            val duration = calculateSnackbarDuration(currentSnackbarData.message)
            isVisible = true
            if (!isNavBarVisible) systemUiController.setNavigationBarColor(snackbarColor)
            delay(duration)
            isVisible = false
            systemUiController.setNavigationBarColor(White)
            delay(AnimationConstants.DefaultDurationMillis.toLong())
            snackbarData.dismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
            .padding(bottom = snackbarPadding)
            .clipToBounds()
    ) {
        currentSnackbarData?.let { snackbarData ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(snackbarColor)
                    .height(64.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = snackbarData.message,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = White,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

class SnackbarHandler(private val scope: CoroutineScope) {

    private val _showSnackBarEvent = MutableSharedFlow<SnackbarData>()
    val showSnackBarEvent: SharedFlow<SnackbarData> = _showSnackBarEvent

    fun showSuccessSnackbar(messageResId: Int? = null, message: String? = null) {
        scope.launch {
            _showSnackBarEvent.emit(
                SnackbarData(
                    messageResId = messageResId, message = message, success = true
                )
            )
        }
    }

    fun showErrorSnackbar(messageResId: Int? = null, message: String? = null) {
        scope.launch {
            _showSnackBarEvent.emit(
                SnackbarData(
                    messageResId = messageResId, message = message, success = false
                )
            )
        }
    }
}

data class SnackbarData(
    val messageResId: Int? = null, val message: String? = null, val success: Boolean
)

/**
 * Calculate snackbar duration basing on message length
 */
private fun calculateSnackbarDuration(message: String) =
    (message.length * 35L).coerceAtLeast(2000L).coerceAtMost(4000L)
