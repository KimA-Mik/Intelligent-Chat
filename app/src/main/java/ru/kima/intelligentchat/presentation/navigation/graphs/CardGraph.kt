package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListScreen
import ru.kima.intelligentchat.presentation.navigation.IntelligentChatNavigationRail
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer

fun NavGraphBuilder.cardGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    expanded: Boolean
) {
    navigation(startDestination = "cards", route = NavItem.Characters.root) {
        composable("cards") {
            val root = @Composable {
                CharactersListScreen(
                    navController,
                    snackbarHostState,
                    drawerState,
                    koinInject(),
                    koinViewModel(),
                    expanded
                )
            }

            when (expanded) {
                true -> Row {
                    IntelligentChatNavigationRail(navController)
                    root()
                }

                false -> NavigationDrawer(
                    drawerState = drawerState,
                    navController = navController,
                ) {
                    root()
                }
            }
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
    this.navigate("cards/${cardId}") { launchSingleTop = true }
