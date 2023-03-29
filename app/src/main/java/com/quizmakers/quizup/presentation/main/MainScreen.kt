package com.quizmakers.quizup.presentation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.quizmakers.quizup.core.navigation.AppNavigation
import com.quizmakers.quizup.core.navigation.NavGraphs
import com.quizmakers.quizup.core.navigation.RootNavGraph
import com.ramcosta.composedestinations.navigation.dependency
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.quizmakers.quizup.ui.common.QuizUpSnackbar
import com.quizmakers.quizup.ui.common.SnackbarHandler

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class,
    ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun MainScreen(
    token: String? = null
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            animationSpec = SwipeableDefaults.AnimationSpec,
            skipHalfExpanded = true
        )
        val bottomSheetNavigator = rememberBottomSheetNavigator(sheetState = sheetState)
        val navController = rememberAnimatedNavController()
        val scope = rememberCoroutineScope()
        val snackbarState = SnackbarHandler(scope)
        navController.navigatorProvider.addNavigator(bottomSheetNavigator)

        Box(modifier = Modifier.fillMaxSize()) {
            ModalBottomSheetLayout(
                modifier = Modifier.navigationBarsPadding(),
                bottomSheetNavigator = bottomSheetNavigator,
                sheetElevation = 0.dp
            ) {
                AppNavigation(
                    navController = navController,
                    onOpenSettings = {
                        dependency(sheetState)
                        dependency(snackbarState)
                    },
                    startRoute = token?.let { NavGraphs.quizzes }
                        ?: NavGraphs.auth,
                    navGraph = RootNavGraph
                )
            }
            QuizUpSnackbar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .align(Alignment.BottomCenter),
                snackbarHandler = snackbarState,
                navController = navController
            )
        }


    }
}

@ExperimentalMaterialNavigationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun rememberBottomSheetNavigator(sheetState: ModalBottomSheetState) = remember(sheetState) {
    BottomSheetNavigator(sheetState = sheetState)
}