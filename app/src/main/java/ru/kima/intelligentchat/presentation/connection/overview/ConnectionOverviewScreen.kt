package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme


@Composable
fun ConnectionOverviewScreen(
    state: ConnectionOverviewState,
    onEvent: (COUserEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "This is a connection screen")
    }
}

@Preview
@Composable
fun ConnectionOverviewPreview() {
    IntelligentChatTheme {
        Surface(Modifier.fillMaxSize()) {
            ConnectionOverviewScreen(
                state = ConnectionOverviewState(),
                onEvent = {}
            )
        }
    }
}