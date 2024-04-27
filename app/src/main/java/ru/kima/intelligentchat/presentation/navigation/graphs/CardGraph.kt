package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreen
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenState
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer

const val CARD_ID_ARGUMENT = "cardId"
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
            route = "cards/{$CARD_ID_ARGUMENT}/edit",
            arguments = listOf(
                navArgument(name = CARD_ID_ARGUMENT) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            CardDetailsScreen(navController, snackbarHostState, koinInject(), koinViewModel())
        }

        composable(
            route = "cards/{$CARD_ID_ARGUMENT}/chat",
            arguments = listOf(
                navArgument(name = CARD_ID_ARGUMENT) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )) {
            val viewModel: ChatScreenViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle(ChatScreenState.ChatState())
            val onEvent = remember<(UserEvent) -> Unit> {
                {
                    viewModel.onEvent(it)
                }
            }

            NavigationDrawer(
                drawerState = drawerState,
                navController = navController
            ) {
                ChatScreen(
                    state = state,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    onEvent = onEvent
                )
            }
        }
    }
}

fun NavController.navigateToCardEdit(cardId: Long) =
    this.navigate("cards/${cardId}/edit") { launchSingleTop = true }

fun NavController.navigateToCardChat(cardId: Long) =
    this.navigate("cards/${cardId}/chat") { launchSingleTop = true }