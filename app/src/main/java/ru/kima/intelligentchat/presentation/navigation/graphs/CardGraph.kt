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
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsRoot
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListScreenRoot
import ru.kima.intelligentchat.presentation.chat.cardChatList.CardChatListScreen
import ru.kima.intelligentchat.presentation.chat.cardChatList.CardChatListState
import ru.kima.intelligentchat.presentation.chat.cardChatList.CardChatListViewModel
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
                CharactersListScreenRoot(
                    expanded = expanded,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState,
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
            CardDetailsRoot(
                navController = navController,
                snackbarHostState = snackbarHostState,
                imagePicker = koinInject(),
                viewModel = koinViewModel()
            )
        }

        composable(
            route = "cards/{$CARD_ID_ARGUMENT}/chat",
            arguments = listOf(
                navArgument(name = CARD_ID_ARGUMENT) {
                    type = NavType.LongType
                    defaultValue = -1L
                })
        ) {
            val viewModel: ChatScreenViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle(ChatScreenState.ChatState())
            val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
            val onEvent = remember<(UserEvent) -> Unit> {
                {
                    viewModel.onEvent(it)
                }
            }

            ChatScreen(
                state = state,
                uiEvent = uiEvent,
                navController = navController,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent
            )
        }

        composable(
            route = "cards/{$CARD_ID_ARGUMENT}/chat/list",
            arguments = listOf(
                navArgument(name = CARD_ID_ARGUMENT) {
                    type = NavType.LongType
                    defaultValue = -1L
                })
        ) {
            val viewModel: CardChatListViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle(CardChatListState())
            val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
            val onEvent =
                remember<(ru.kima.intelligentchat.presentation.chat.cardChatList.events.UserEvent) -> Unit> {
                    {
                        viewModel.onEvent(it)
                    }
                }

            CardChatListScreen(
                state = state,
                uiEvent = uiEvent,
                navController = navController,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
            )
        }
    }
}

fun NavController.navigateToCardEdit(cardId: Long) =
    this.navigate("cards/${cardId}/edit") { launchSingleTop = true }

fun NavController.navigateToCardChat(cardId: Long) =
    this.navigate("cards/${cardId}/chat") { launchSingleTop = true }

fun NavController.navigateToCardChatList(cardId: Long) =
    this.navigate("cards/${cardId}/chat/list") { launchSingleTop = true }