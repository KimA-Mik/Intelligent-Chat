package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatScreenState,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    onEvent: (UserEvent) -> Unit
) {
    when (state) {
        is ChatScreenState.ChatState -> ChatScreenContent(
            state = state,
            drawerState = drawerState,
            snackbarHostState = snackbarHostState,
            onEvent = onEvent
        )

        ChatScreenState.ErrorState -> TODO()
    }
}



