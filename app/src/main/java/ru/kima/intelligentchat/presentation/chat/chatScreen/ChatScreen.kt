package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

@Composable
fun ChatScreenRoot(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val viewModel: ChatScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(ChatState())
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
    val onEvent = remember<(UserEvent) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    ChatScreenContent(
        state = state,
        uiEvent = uiEvent,
        navController = navController,
        snackbarHostState = snackbarHostState,
        onEvent = onEvent
    )
}




