package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.components.TitledFiniteSlider
import ru.kima.intelligentchat.presentation.ui.components.TitledFloatSlider
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HordePresetEditScreen(
    state: HordePresetEditScreenState,
    onEvent: (UserEvent) -> Unit,
    popBack: () -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//    val sb = remember { scrollBehavior }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Generation preset",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = popBack) {
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
            .nestedScroll(scrollBehavior.nestedScrollConnection)

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
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.preset.name, onValueChange = {
                onEvent(UserEvent.EditTitle(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            label = { Text(text = stringResource(R.string.title_label)) },
            singleLine = true
        )

        TitledFloatSlider(
            title = stringResource(R.string.temperature_title),
            value = state.preset.temperature,
            leftBorder = 0f,
            rightBorder = 4f,
            updateValue = { onEvent(UserEvent.EditTemperature(it)) },
            tooltipText = stringResource(R.string.temperature_tooltip)
        )

        TitledFiniteSlider(
            title = stringResource(R.string.top_k_title),
            value = state.preset.topK,
            leftBorder = 0,
            rightBorder = 100,
            updateValue = { onEvent(UserEvent.EditTopK(it)) },
            tooltipText = stringResource(R.string.top_k_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.top_p_title),
            value = state.preset.topP,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTopP(it)) },
            tooltipText = stringResource(R.string.top_p_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.typical_p_title),
            value = state.preset.typical,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTypical(it)) },
            tooltipText = stringResource(R.string.typical_p_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.min_p_title),
            value = state.preset.minP,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditMinP(it)) },
            tooltipText = stringResource(R.string.min_p_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.top_a_title),
            value = state.preset.topA,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTopA(it)) },
            tooltipText = stringResource(R.string.top_a_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.tail_free_sampling_title),
            value = state.preset.tailFreeSampling,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTailFreeSampling(it)) },
            tooltipText = stringResource(R.string.tail_free_sampling_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.repetition_penalty_title),
            value = state.preset.repetitionPenalty,
            leftBorder = 1f,
            rightBorder = 3f,
            updateValue = { onEvent(UserEvent.EditRepetitionPenalty(it)) }
        )

        TitledFiniteSlider(
            title = stringResource(R.string.repetition_penalty_range_title),
            value = state.preset.repetitionPenaltyRange,
            leftBorder = 0,
            rightBorder = 8192,
            updateValue = { onEvent(UserEvent.EditRepetitionPenaltyRange(it)) }
        )

        TitledFloatSlider(
            title = stringResource(R.string.repetition_penalty_slope_title),
            value = state.preset.repetitionPenaltySlope,
            leftBorder = 0f,
            rightBorder = 10f,
            updateValue = { onEvent(UserEvent.EditRepetitionPenaltySlope(it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))
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
                state = HordePresetEditScreenState.Preset(),
                onEvent = {},
                popBack = {},
            )
        }
    }
}