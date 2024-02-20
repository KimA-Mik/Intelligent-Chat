package ru.kima.intelligentchat.presentation.connection.overview.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewState
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun HordeFragment(
    state: ConnectionOverviewState.HordeFragmentState,
    modifier: Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SimpleConfig(
            contextToWorker = state.contextToWorker,
            responseToWorker = state.responseToWorker,
            trustedWorkers = state.trustedWorkers,
            onEvent = onEvent
        )

        ApiKeyField(
            currentApiToken = state.currentApiToken,
            showApiToken = state.showApiToken,
            onEvent = onEvent
        )

        Models()
    }
}

@Composable
fun SimpleConfig(
    contextToWorker: Boolean,
    responseToWorker: Boolean,
    trustedWorkers: Boolean,
    onEvent: (COUserEvent) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = contextToWorker, onCheckedChange = {
                onEvent(COUserEvent.ToggleContextToWorker)
            })
            Text(
                text = "Adjust context size to worker capabilities",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = responseToWorker, onCheckedChange = {
                onEvent(COUserEvent.ToggleResponseToWorker)
            })
            Text(
                text = "Adjust response length to worker capabilities",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = trustedWorkers, onCheckedChange = {
                onEvent(COUserEvent.ToggleTrustedWorkers)
            })
            Text(
                text = "Trusted workers only",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = "Context: --, Response: --", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ApiKeyField(
    currentApiToken: String,
    showApiToken: Boolean,
    onEvent: (COUserEvent) -> Unit
) {
    Text(
        text = "API key",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = currentApiToken,
            onValueChange = { onEvent(COUserEvent.UpdateApiToken(it)) },
            modifier = Modifier
                .weight(1f),
            label = { Text(text = "Horde API token") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { onEvent(COUserEvent.ToggleHordeTokenVisibility) }) {
                    Icon(
                        imageVector = if (showApiToken) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Show / Hide api key"
                    )
                }
            },
            visualTransformation = if (showApiToken) VisualTransformation.None else PasswordVisualTransformation()
        )

        IconButton(
            onClick = { onEvent(COUserEvent.SaveApiKey) },
        ) {
            Icon(imageVector = Icons.Filled.Save, contentDescription = "")
        }
    }
}

@Composable
fun Models() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Models",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = { /*TODO*/ }, modifier = Modifier) {
            (Text(text = "Select models"))
        }

    }
    ListItem(headlineContent = { Text(text = "Model 1") })
    ListItem(headlineContent = { Text(text = "Model 2") })
}

@Preview
@Composable
fun HordeFragmentPreview() {
    IntelligentChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HordeFragment(
                state = ConnectionOverviewState.HordeFragmentState(),
                modifier = Modifier,
                onEvent = {})
        }
    }
}