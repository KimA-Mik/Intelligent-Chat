package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatScreenState,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onEvent: (UserEvent) -> Unit
) {
    when (state) {
        is ChatScreenState.ChatState -> ChatScreenContent(
            state = state,
            navController = navController,
            snackbarHostState = snackbarHostState,
            onEvent = onEvent
        )

        ChatScreenState.ErrorState -> TODO()
    }
}



