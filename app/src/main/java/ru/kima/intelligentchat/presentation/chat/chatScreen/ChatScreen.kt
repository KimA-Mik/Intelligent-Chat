package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

@Composable
fun ChatScreen(
    state: ChatScreenState,
    onEvent: (UserEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Now selected persona is ${state.selectedPersona}")
    }
}