package com.quizmakers.quizup.presentation.main

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.quizmakers.quizup.presentation.destinations.DashboardScreenDestination
import com.quizmakers.quizup.presentation.destinations.SignInScreenDestination
import com.quizmakers.quizup.presentation.destinations.SignOutScreenDestination
import com.quizmakers.quizup.ui.theme.DarkBlue
import com.quizmakers.quizup.ui.theme.MediumBlue
import com.ramcosta.composedestinations.navigation.clearBackStack
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.spec.Route

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val currentRoute = navBackStackEntry?.destination?.route
                arrayListOf("", " ").forEach { screen ->
                    BottomNavigation(
                        backgroundColor = DarkBlue,
                        elevation = 0.dp,
                        modifier = Modifier.height(56.dp)
                    ) {
                        BottomNavigationItems.values().forEach { item ->
                            val route = item.direction.route
                            val rootBottomNavItemRoute = navController
                                .currentBackStack
                                .value
                                .findLast { entry ->
                                    BottomNavigationItems.values()
                                        .map { it.direction.route }
                                        .contains(entry.destination.route)
                                }
                                ?.destination
                                ?.route
                            val isSelected = rootBottomNavItemRoute == route
                            BottomNavigationItem(
                                unselectedContentColor = Color.White,
                                selectedContentColor = MediumBlue,
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = Icons.Default.ArrowBack.name,
                                        tint = if (isSelected) MediumBlue else Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                label = {
                                    Text(text = "label 1")
                                },
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        navController.popBackStack(route, false)
                                    } else {
                                        navController.navToBottomNavItem(
                                            route,
                                            popUpTo = DashboardScreenDestination
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content()
            }
        }
    )
}

fun NavController.navToBottomNavItem(
    route: String,
    stackToClearRoute: Route? = null,
    popUpTo: DirectionDestinationSpec
) {

    stackToClearRoute?.let {
        if (!clearBackStack(it)) {
            popBackStack(it, inclusive = true, saveState = false)
        }
    }

    navigate(route) {
        popUpTo(popUpTo) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

enum class BottomNavigationItems(
    val direction: Route,
    @StringRes val label: Int
) {
    Dashboard(
        direction = DashboardScreenDestination,
        label = 23
    ),
    SignIn(
        direction = SignInScreenDestination,
        label = 23
    ),
    SignOut(
        direction = SignOutScreenDestination,
        label = 23
    ),
}