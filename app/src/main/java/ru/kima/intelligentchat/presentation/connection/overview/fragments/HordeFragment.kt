package ru.kima.intelligentchat.presentation.connection.overview.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewState
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeDialogActiveModel
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun HordeFragment(
    state: ConnectionOverviewState.HordeFragmentState,
    modifier: Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    when {
        state.showSelectHordeModelsDialog -> SelectHordeModelsAlertDialog(
            dialogActiveModels = state.dialogSelectedModels,
            onEvent = onEvent
        )
    }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SimpleConfig(
            contextToWorker = state.contextToWorker,
            responseToWorker = state.responseToWorker,
            trustedWorkers = state.trustedWorkers,
            contextSize = state.contextSize,
            responseLength = state.responseLength,
            onEvent = onEvent
        )

        ApiKeyField(
            currentApiToken = state.currentApiToken,
            showApiToken = state.showApiToken,
            userName = state.userName.ifBlank { stringResource(id = R.string.anonymous_username) },
            onEvent = onEvent
        )

        Models(
            selectedModels = state.selectedModels,
            onEvent = onEvent
        )
    }
}

@Composable
fun SimpleConfig(
    contextToWorker: Boolean,
    responseToWorker: Boolean,
    trustedWorkers: Boolean,
    contextSize: Int,
    responseLength: Int,
    onEvent: (COUserEvent) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = contextToWorker, onCheckedChange = {
                onEvent(COUserEvent.ToggleContextToWorker)
            })
            Text(
                text = stringResource(R.string.adjust_context_size),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = responseToWorker, onCheckedChange = {
                onEvent(COUserEvent.ToggleResponseToWorker)
            })
            Text(
                text = stringResource(R.string.adjust_response_length),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = trustedWorkers, onCheckedChange = {
                onEvent(COUserEvent.ToggleTrustedWorkers)
            })
            Text(
                text = stringResource(R.string.trusted_workers_only),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        val contextField = if (contextSize > 0) contextSize.toString() else "--"
        val responseField = if (responseLength > 0) responseLength.toString() else "--"
        Text(
            text = stringResource(id = R.string.context_and_response, contextField, responseField),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ApiKeyField(
    currentApiToken: String,
    showApiToken: Boolean,
    userName: String,
    onEvent: (COUserEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.api_key),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        TextButton(onClick = { onEvent(COUserEvent.ShowKudos) }) {
            Text(text = stringResource(R.string.show_kudos_label))
        }
    }
    Text(
        text = stringResource(id = R.string.username_template, userName),
        style = MaterialTheme.typography.bodyLarge
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
            label = { Text(text = stringResource(R.string.horde_api_token)) },
            placeholder = { Text(text = "0000000000") },
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
fun Models(
    selectedModels: List<String>,
    onEvent: (COUserEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Models",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { onEvent(COUserEvent.RefreshModels) }) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = { onEvent(COUserEvent.OpenSelectHordeModelsDialog) },
            modifier = Modifier
        ) {
            (Text(text = "Select models"))
        }

    }

    selectedModels.forEach {
        ListItem(headlineContent = { Text(text = it) })
    }
}

@Composable
fun SelectHordeModelsAlertDialog(
    dialogActiveModels: List<HordeDialogActiveModel>,
    onEvent: (COUserEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(COUserEvent.DismissSelectHordeModelsDialog) },
        confirmButton = {
            TextButton(onClick = { onEvent(COUserEvent.AcceptSelectHordeModelsDialog) }) {
                Text(text = "Accept")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(COUserEvent.DismissSelectHordeModelsDialog) }) {
                Text(text = "Dismiss")
            }
        },
        title = {
            Text(text = "Select horde models")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dialogActiveModels, key = { it.name }) {
                    ActiveModelItem(
                        model = it,
                        onEvent = onEvent
                    )
                }
            }
        },
    )
}

@Composable
fun ActiveModelItem(
    model: HordeDialogActiveModel,
    onEvent: (COUserEvent) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = model.selected,
            onCheckedChange = { onEvent(COUserEvent.CheckHordeModel(model.name)) })
        Text(text = "${model.name} ${model.details}")
    }
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