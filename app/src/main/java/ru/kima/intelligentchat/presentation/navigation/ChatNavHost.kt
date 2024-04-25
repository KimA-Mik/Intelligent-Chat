package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.navigation.graphs.cardGraph
import ru.kima.intelligentchat.presentation.navigation.graphs.chatGraph
import ru.kima.intelligentchat.presentation.navigation.graphs.connectionGraph
import ru.kima.intelligentchat.presentation.navigation.graphs.personasGraph
import ru.kima.intelligentchat.presentation.showImage.ShowImageScreen


@Composable
fun ChatNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
) {
    NavHost(navController = navController, startDestination = NavItem.entries.first().root) {
        cardGraph(navController, snackbarHostState, drawerState)
        personasGraph(navController, snackbarHostState, drawerState)
        connectionGraph(navController, snackbarHostState, drawerState)
        chatGraph(navController, snackbarHostState, drawerState)
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

//Funny mess
//fun NavHostController.navigateFromRoot(navBackStackEntry: NavBackStackEntry, route: String) {
//    if (navBackStackEntry.lifecycleIsResumed()) {
////        popBackStack(graph.startDestinationId, inclusive = true)
//
//        while (true) {
//            if (!popBackStack())
//                break
//        }
//        //https://developer.android.com/jetpack/compose/navigation#bottom-nav
//        navigate(route) {
////            popUpTo(graph.findStartDestination().id) {
////                saveState = true
////            }
//            launchSingleTop = true
////            restoreState = true
//        }
//    }
//}

fun NavController.navigateToCardImage(cardId: Long) =
    this.navigate("image/${cardId}")

//private fun NavBackStackEntry.lifecycleIsResumed() =
//    this.lifecycle.currentState == Lifecycle.State.RESUMED
