package ru.kima.intelligentchat.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.charactersList.CharactersListScreen
import ru.kima.intelligentchat.presentation.showImage.ShowImageScreen

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ApplicationScreen(
) {
    KoinAndroidContext {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        NavHost(navController = navController, startDestination = "cards") {
            composable("cards") {
                CharactersListScreen(
                    navController, snackbarHostState, koinInject(), koinViewModel()
                )
            }
            composable(
                route = "cards/{cardId}",
                arguments = listOf(
                    navArgument(name = "cardId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                CardDetailsScreen(navController, snackbarHostState, koinInject(), koinViewModel())
            }
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
}