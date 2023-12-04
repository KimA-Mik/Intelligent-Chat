package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.charactersList.CharactersListScreen

fun NavGraphBuilder.cardGraph(navController: NavController, snackbarHostState: SnackbarHostState) {
    navigation(startDestination = "cards", route = "cardsScreen") {
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
    }
}

fun NavController.navigateToCard(cardId: Long) =
    this.navigate("cards/${cardId}")

fun NavHostController.navigateToCardsScreen() =
    this.navigateSingleTopTo("cardsScreen")