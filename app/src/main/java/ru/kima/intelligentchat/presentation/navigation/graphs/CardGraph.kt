package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
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
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreen
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenState
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationLayout

const val CARD_ID_ARGUMENT = "cardId"
fun NavGraphBuilder.cardGraph(
    expanded: Boolean,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState
) {
    navigation(startDestination = "cards", route = NavItem.Characters.root) {
        composable("cards") {
            NavigationLayout(
                expanded = expanded,
                navController = navController,
                drawerState = drawerState
            ) {
                CharactersListScreen(
                    navController,
                    snackbarHostState,
                    drawerState,
                    koinInject(),
                    koinViewModel(),
                    expanded
                )
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

            ChatScreen(
                state = state,
                navController = navController,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent
            )
        }
    }
}

fun NavController.navigateToCardEdit(cardId: Long) =
    this.navigate("cards/${cardId}/edit") { launchSingleTop = true }

fun NavController.navigateToCardChat(cardId: Long) =
    this.navigate("cards/${cardId}/chat") { launchSingleTop = true }