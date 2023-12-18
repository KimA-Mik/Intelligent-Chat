package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.navigation.graphs.cardGraph
import ru.kima.intelligentchat.presentation.navigation.graphs.personasGraph
import ru.kima.intelligentchat.presentation.showImage.ShowImageScreen
import ru.kima.intelligentchat.presentation.ui.MainActivityViewModel


@Composable
fun ChatNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    drawerSelected: Int,
    onEvent: (MainActivityViewModel.UserEvent) -> Unit
) {
    NavHost(navController = navController, startDestination = NavItem.entries.first().root) {
        cardGraph(navController, snackbarHostState, drawerState, drawerSelected, onEvent)
        personasGraph(navController, snackbarHostState, drawerState, drawerSelected, onEvent)
        composable(route = "image/{cardId}", arguments = listOf(
            navArgument(name = "cardId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )) {
            ShowImageScreen(navController, snackbarHostState, koinViewModel())
        }
    }
}

fun NavHostController.navigateFromRoot(navBackStackEntry: NavBackStackEntry, route: String) {
    if (navBackStackEntry.lifecycleIsResumed()) {
//        popBackStack(graph.startDestinationId, inclusive = true)

        //TODO: change this shit to actual working solution
        while (true) {
            if (!popBackStack())
                break
        }
        //https://developer.android.com/jetpack/compose/navigation#bottom-nav
        //Todo: find out how to fix google example code
        navigate(route) {
//            popUpTo(graph.findStartDestination().id) {
//                saveState = true
//            }
            launchSingleTop = true
//            restoreState = true
        }
    }
}

fun NavController.navigateToCardImage(cardId: Long) =
    this.navigate("image/${cardId}")

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
