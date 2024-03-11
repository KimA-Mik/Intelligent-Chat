package ru.kima.intelligentchat.presentation.connection.overview.fragments

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewState
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeDialogActiveModel
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

private val cardPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)

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
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            SimpleConfig(
                contextToWorker = state.contextToWorker,
                responseToWorker = state.responseToWorker,
                trustedWorkers = state.trustedWorkers,
                contextSize = state.contextSize,
                responseLength = state.responseLength,
                onEvent = onEvent
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            GenerationConfig(
                contextSize = state.contextSize,
                responseLength = state.responseLength,
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            ApiKeyField(
                currentApiToken = state.currentApiToken,
                showApiToken = state.showApiToken,
                userName = state.userName.ifBlank { stringResource(id = R.string.anonymous_username) },
                onEvent = onEvent
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            Models(
                selectedModels = state.selectedModels,
                onEvent = onEvent
            )
        }
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
    Column(
        modifier = Modifier
            .padding(cardPadding)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.adjust_context_size),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Switch(
                checked = contextToWorker, onCheckedChange = {
                    onEvent(COUserEvent.ToggleContextToWorker)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.adjust_response_length),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = responseToWorker, onCheckedChange = {
                    onEvent(COUserEvent.ToggleResponseToWorker)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.trusted_workers_only),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = trustedWorkers, onCheckedChange = {
                    onEvent(COUserEvent.ToggleTrustedWorkers)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
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
    Column(
        modifier = Modifier.padding(cardPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.api_key),
                style = MaterialTheme.typography.titleLarge,
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
            OutlinedTextField(
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
}

@Composable
fun Models(
    selectedModels: List<String>,
    onEvent: (COUserEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(cardPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Models",
                style = MaterialTheme.typography.titleLarge,
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
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
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
            LazyColumn {
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
fun GenerationConfig(
    contextSize: Int,
    responseLength: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        DetailedSlider(
            title = "Response (tokens)",
            value = 0,
            leftBorder = 16f,
            rightBorder = 2048f,
            updateValue = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        DetailedSlider(
            title = "Context (tokens)",
            value = 0,
            leftBorder = 512f,
            rightBorder = 8196f,
            updateValue = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        val contextField = if (contextSize > 0) contextSize.toString() else "--"
        val responseField = if (responseLength > 0) responseLength.toString() else "--"
        Text(
            text = stringResource(id = R.string.context_and_response, contextField, responseField),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedSlider(
    title: String,
    value: Int,
    leftBorder: Float,
    rightBorder: Float,
    updateValue: (Float) -> Unit,

    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = value.toString(), onValueChange = {
                    updateValue(it.toFloat())
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier
                    .width(100.dp)
                    .wrapContentWidth()
            )
        }
        val state = remember(leftBorder, rightBorder) {
            SliderState(
                value = 0f,
                steps = (rightBorder - leftBorder).toInt(),
                valueRange = leftBorder..rightBorder
            )
        }
        Slider(state)

        val steps = remember(leftBorder, rightBorder) {
            (rightBorder - leftBorder).toInt()
        }

        Slider(
            value = 0f,
            valueRange = leftBorder..rightBorder,
            steps = steps,
            onValueChange = updateValue
        )

    }
}

@Composable
fun ActiveModelItem(
    model: HordeDialogActiveModel,
    onEvent: (COUserEvent) -> Unit
) {
    ListItem(
        trailingContent = {
            Checkbox(
                checked = model.selected,
                onCheckedChange = { onEvent(COUserEvent.CheckHordeModel(model.name)) })
        },
        headlineContent = { Text(text = "${model.name} ${model.details}") }
    )
}


@Preview(name = "Light Mode")
@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
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