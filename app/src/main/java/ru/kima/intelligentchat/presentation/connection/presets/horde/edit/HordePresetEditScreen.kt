package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun HordePresetEditScreen(
    state: HordePresetEditScreenState,
    onEvent: (UserEvent) -> Unit
) {
    Scaffold { paddingValues ->
        HordePresetEditContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
fun HordePresetEditContent(
    state: HordePresetEditScreenState,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {

    }
}

@Preview
@Composable
private fun HordePresetEditScreenPreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HordePresetEditScreen(state = HordePresetEditScreenState()) {

            }
        }
    }
}