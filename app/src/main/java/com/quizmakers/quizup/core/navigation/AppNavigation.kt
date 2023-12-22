package com.quizmakers.quizup.core.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.quizmakers.quizup.presentation.destinations.*
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    engine: NavHostEngine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { defaultSortEnterTransition(initialState, targetState) },
            exitTransition = { defaultSortExitTransition(initialState, targetState) },
            popEnterTransition = { defaultTiviPopEnterTransition() },
            popExitTransition = { defaultTiviPopExitTransition() },
        )
    ),
    navController: NavHostController,
    onOpenSettings: @Composable DependenciesContainerBuilder<*>.() -> Unit = {},
    startRoute: Route,
    navGraph: NavGraphSpec,

    ) {
    DestinationsNavHost(
        engine = engine,
        navController = navController,
        navGraph = navGraph,
        modifier = modifier,
        startRoute = startRoute,
        dependenciesContainerBuilder = onOpenSettings
    )
}

enum class AppRouteEnum(var routString: String) {
    AUTH("auth"), QUIZZES("quizzes"), ROOT("root")
}

object NavGraphs {

    val auth = object : NavGraphSpec {
        override val route = AppRouteEnum.AUTH.routString

        override val startRoute = SignInScreenDestination

        override val destinationsByRoute = listOf(
            SignInScreenDestination,
            SignOutScreenDestination
        ).associateBy { it.route }
    }
    val quizzes = object : NavGraphSpec {
        override val route = AppRouteEnum.QUIZZES.routString

        override val startRoute = DashboardScreenDestination

        override val destinationsByRoute = listOf(
            DashboardScreenDestination,
            BattleScreenDestination,
            QuizManagerScreenDestination,
            QuizDetailsBottomSheetDestination,
            QuizScreenDestination,
            GenerateQuizAiScreenDestination
        ).associateBy { it.route }
    }
}

@ExperimentalComposeUiApi
object RootNavGraph : NavGraphSpec {

    override val route = AppRouteEnum.ROOT.routString

    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

    override val startRoute = NavGraphs.auth

    override val nestedNavGraphs = listOf(
        NavGraphs.auth,
        NavGraphs.quizzes
    )
}

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultSortEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

@ExperimentalAnimationApi

private fun AnimatedContentTransitionScope<*>.defaultSortExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultTiviPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultTiviPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
}
