package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.showImage.ShowImageScreen

@Composable
fun ChatNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(navController = navController, startDestination = "cardsScreen") {
        cardGraph(navController, snackbarHostState)
        composable(route = "image/{imageName}", arguments = listOf(
            navArgument(name = "imageName") {
                type = NavType.StringType
                defaultValue = String()
            }
        )) {
            ShowImageScreen(navController, snackbarHostState, koinViewModel())
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

fun NavController.navigateToImage(imageName: String) =
    this.navigate("image/${imageName}")
