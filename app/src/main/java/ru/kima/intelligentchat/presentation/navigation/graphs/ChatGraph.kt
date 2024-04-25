package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreen
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenState
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer

fun NavGraphBuilder.chatGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
) = navigation(startDestination = "chatScreen", route = NavItem.Chat.root) {
    composable("chatScreen") {
        val viewModel: ChatScreenViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle(ChatScreenState())
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
                drawerState = drawerState,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent
            )
        }
    }
}