package ru.kima.intelligentchat.presentation.connection.overview.fragments

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeModelsWrapper
import ru.kima.intelligentchat.presentation.connection.overview.model.HordePreset
import ru.kima.intelligentchat.presentation.connection.overview.model.HordePresetsWrapper
import ru.kima.intelligentchat.presentation.ui.components.TitledFiniteSlider
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

private val cardNoTopPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)

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
                wrapper = state.presetsWrapper,
                onEvent = onEvent,
                modifier = Modifier.padding(8.dp)
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            GenerationConfig(
                actualContextSize = state.actualContextSize,
                userContextSize = state.userContextSize,
                actualResponseLength = state.actualResponseLength,
                userResponseLength = state.userResponseLength,
                onEvent = onEvent,
                modifier = Modifier.padding(8.dp)
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
                onEvent = onEvent,
                modifier = Modifier.padding(cardNoTopPadding)
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            Models(
                wrapper = state.selectedModelsWrapper,
                onEvent = onEvent,
                modifier = Modifier.padding(cardNoTopPadding)
            )
        }
    }
}

@Composable
fun SimpleConfig(
    contextToWorker: Boolean,
    responseToWorker: Boolean,
    trustedWorkers: Boolean,
    wrapper: HordePresetsWrapper,
    onEvent: (COUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TitledSwitch(
            title = stringResource(R.string.adjust_context_size),
            checked = contextToWorker,
            onCheckedChange = { onEvent(COUserEvent.ToggleContextToWorker) },
            modifier = Modifier.fillMaxWidth()
        )

        TitledSwitch(
            title = stringResource(R.string.adjust_response_length),
            checked = responseToWorker,
            onCheckedChange = { onEvent(COUserEvent.ToggleResponseToWorker) },
            modifier = Modifier.fillMaxWidth()
        )

        TitledSwitch(
            title = stringResource(R.string.trusted_workers_only),
            checked = trustedWorkers,
            onCheckedChange = { onEvent(COUserEvent.ToggleTrustedWorkers) },
            modifier = Modifier.fillMaxWidth()
        )

        GenPresetSelector(
            wrapper = wrapper,
            onEvent = onEvent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun GenerationConfig(
    actualContextSize: Int,
    userContextSize: Int,
    actualResponseLength: Int,
    userResponseLength: Int,
    modifier: Modifier = Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        TitledFiniteSlider(
            title = stringResource(R.string.response_label),
            value = userResponseLength,
            leftBorder = 16,
            rightBorder = 2048,
            updateValue = {
                onEvent(COUserEvent.UpdateHordeResponseLength(it))
            },
            modifier = Modifier.fillMaxWidth(),
            textFieldLabel = stringResource(R.string.tokens_label)
        )

        TitledFiniteSlider(
            title = stringResource(R.string.context_label),
            value = userContextSize,
            leftBorder = 512,
            rightBorder = 8196,
            updateValue = {
                onEvent(COUserEvent.UpdateHordeContextSize(it))
            },
            modifier = Modifier.fillMaxWidth(),
            textFieldLabel = stringResource(R.string.tokens_label)
        )

        val contextField = if (actualContextSize > 0) actualContextSize.toString() else "--"
        val responseField = if (actualResponseLength > 0) actualResponseLength.toString() else "--"
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
    onEvent: (COUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
    wrapper: HordeModelsWrapper,
    onEvent: (COUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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

        wrapper.selectedModels.forEach {
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
fun TitledSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked, onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun GenPresetSelector(
    wrapper: HordePresetsWrapper,
    onEvent: (COUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(1f)) {
            var showMenu by remember { mutableStateOf(false) }
            val rotation by animateFloatAsState(
                targetValue = if (showMenu) 0f else 180f,
                label = "rotation"
            )
            TextField(
                value = wrapper.preset.name, onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropUp, contentDescription = "",
                            modifier = Modifier.graphicsLayer(
                                rotationZ = rotation
                            )
                        )
                    }
                }
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }) {
                wrapper.presets.forEach { preset ->
                    DropdownMenuItem(
                        text = { Text(text = preset.name) },
                        onClick = {
                            showMenu = false
                            onEvent(COUserEvent.SelectHordePreset(preset.id))
                        }
                    )
                }
            }
        }

        IconButton(
            onClick = { onEvent(COUserEvent.EditPreset) },
        ) {
            Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
        }
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
        headlineContent = { Text(text = "${model.name} ${model.details}") },
        colors = ListItemDefaults.colors(containerColor = AlertDialogDefaults.containerColor)
    )
}


@Preview(name = "Light Mode")
@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HordeFragmentPreview() {
    IntelligentChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HordeFragment(
                state = ConnectionOverviewState.HordeFragmentState(
                    presetsWrapper = HordePresetsWrapper(HordePreset(1, "Preset"))
                ),
                modifier = Modifier,
                onEvent = {})
        }
    }
}