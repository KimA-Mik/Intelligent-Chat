package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.components.TitledFiniteSlider
import ru.kima.intelligentchat.presentation.ui.components.TitledFloatSlider
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HordePresetEditScreen(
    navController: NavController,
    state: HordePresetEditScreenState,
    onEvent: (UserEvent) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//    val sb = remember { scrollBehavior }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.nav_item_connection),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .imePadding(),
    ) { paddingValues ->
        val modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        when (state) {
            HordePresetEditScreenState.NoPreset -> NoPreset(
                onEvent = onEvent,
                modifier = modifier
            )

            is HordePresetEditScreenState.Preset -> Preset(
                state = state,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
fun Preset(
    state: HordePresetEditScreenState.Preset,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.preset.name, onValueChange = {
                onEvent(UserEvent.EditTitle(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Title") },
            singleLine = true
        )

        HorizontalDivider()

        TitledFloatSlider(
            title = "Temperature",
            value = state.preset.temperature,
            leftBorder = 0f,
            rightBorder = 4f,
            updateValue = { onEvent(UserEvent.EditTemperature(it)) },
            tooltipText = "Temperature controls the randomness in token selection:\n- low temperature (<1.0) leads to more predictable text, favoring higher probability tokens.\n- high temperature (>1.0) increases creativity and diversity in the output by giving lower probability tokens a better chance.\nSet to 1.0 for the original probabilities."
        )

        TitledFiniteSlider(
            title = "Top K",
            value = state.preset.topK,
            leftBorder = 0,
            rightBorder = 100,
            updateValue = { onEvent(UserEvent.EditTopK(it)) },
            tooltipText = "Top K sets a maximum amount of top tokens that can be chosen from.\nE.g Top K is 20, this means only the 20 highest ranking tokens will be kept (regardless of their probabilities being diverse or limited).\nSet to 0 to disable."
        )

        TitledFloatSlider(
            title = "Top P",
            value = state.preset.topP,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTopP(it)) },
            tooltipText = "Top P (a.k.a. nucleus sampling) adds up all the top tokens required to add up to the target percentage.\nE.g If the Top 2 tokens are both 25%, and Top P is 0.50, only the Top 2 tokens are considered.\nSet to 1.0 to disable."
        )
    }
}

@Composable
fun NoPreset(
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No preset")
    }
}

@Preview
@Composable
private fun HordePresetEditScreenPreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HordePresetEditScreen(
                navController = rememberNavController(),
                state = HordePresetEditScreenState.Preset(),
                onEvent = {}
            )
        }
    }
}