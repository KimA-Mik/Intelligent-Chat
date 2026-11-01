package ru.kima.intelligentchat.presentation.connection.overview.fragments

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewState
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent

@Composable
fun TextCompletionFragment(
    state: ConnectionOverviewState.TextCompletionFragmentState,
    modifier: Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 1.dp) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.koboldcpp_connection_title),
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = state.baseUrl,
                    onValueChange = { onEvent(COUserEvent.UpdateTextCompletionBaseUrl(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.koboldcpp_base_url)) },
                    placeholder = { Text("http://192.168.1.10:5001/") },
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.koboldcpp_base_url_hint),
                    style = MaterialTheme.typography.bodySmall
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        var expanded by remember { mutableStateOf(false) }
                        val rotation by animateFloatAsState(
                            targetValue = if (expanded) 0f else 180f,
                            label = "text completion preset menu rotation"
                        )
                        TextField(
                            value = state.presetsWrapper.preset.name,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropUp,
                                        contentDescription = null,
                                        modifier = Modifier.graphicsLayer(rotationZ = rotation)
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.presetsWrapper.presets.forEach { preset ->
                                DropdownMenuItem(
                                    text = { Text(preset.name) },
                                    onClick = {
                                        expanded = false
                                        onEvent(COUserEvent.SelectTextCompletionPreset(preset.id))
                                    }
                                )
                            }
                        }
                    }
                    if (state.presetsWrapper.preset.id != 0L) {
                        IconButton(
                            onClick = { onEvent(COUserEvent.EditTextCompletionPreset) }
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(R.string.edit_preset)
                            )
                        }
                    }
                }
            }
        }
    }
}
