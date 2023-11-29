package ru.kima.intelligentchat.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.charactersList.CharactersListScreen

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ApplicationScreen(
) {
    KoinAndroidContext {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "cards") {
            composable("cards") { CharactersListScreen(navController) }
            composable(
                route = "cards/{cardId}",
                arguments = listOf(
                    navArgument(name = "cardId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                CardDetailsScreen(navController)
            }
        }
    }
}