package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.common.util.dpToPx
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.components.TitledFiniteSlider
import ru.kima.intelligentchat.presentation.ui.components.TitledFloatSlider
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HordePresetEditScreen(
    state: HordePresetEditScreenState,
    onEvent: (UserEvent) -> Unit,
    popBack: () -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                actions = {
                    SimpleDropdownMenu(menuItems = dropDownMenuItems(onEvent))
                },
                scrollBehavior = remember { scrollBehavior },
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
private fun dropDownMenuItems(
    onEvent: (UserEvent) -> Unit
) = remember {
    listOf(
        SimpleDropDownMenuItem(
            ComposeString.Resource(R.string.save_preset),
            onClick = { onEvent(UserEvent.SavePreset) },
            iconVector = Icons.Filled.Save
        )
    )
}

private val samplers = listOf(
    R.string.top_k_title,
    R.string.top_a_title,
    R.string.top_and_min_p_title,
    R.string.tail_free_sampling_title,
    R.string.typical_p_title,
    R.string.temperature_title,
    R.string.repetition_penalty_title,
)

@Composable
fun Preset(
    state: HordePresetEditScreenState.Preset,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            Sliders(
                name = state.preset.name,
                temperature = state.preset.temperature,
                topK = state.preset.topK,
                topP = state.preset.topP,
                typical = state.preset.typical,
                minP = state.preset.minP,
                topA = state.preset.topA,
                tailFreeSampling = state.preset.tailFreeSampling,
                repetitionPenalty = state.preset.repetitionPenalty,
                repetitionPenaltyRange = state.preset.repetitionPenaltyRange,
                repetitionPenaltySlope = state.preset.repetitionPenaltySlope,
                mirostat = state.preset.mirostat,
                mirostatTau = state.preset.mirostatTau,
                mirostatEta = state.preset.mirostatEta,
                onEvent = onEvent,
            )
        }

        itemsIndexed(state.preset.samplerOrder, key = { _, item ->
            item
        }) { index, sampler ->
            val id = samplers.getOrElse(sampler) { -1 }

            if (id < 0) return@itemsIndexed
            CardsListElement(
                itemCount = state.preset.samplerOrder.size,
                index = index,
                sampler = sampler,
                samplerNameId = id,
                onEvent = onEvent,
                modifier = Modifier.animateItem()
            )
        }
    }
}


@Composable
fun CardsListElement(
    itemCount: Int,
    index: Int,
    sampler: Int,
    samplerNameId: Int,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val elementSize = 48.dp.dpToPx()
    var offsetY by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(index) {
        if (offsetY > elementSize) offsetY -= elementSize
        if (offsetY < -elementSize) offsetY += elementSize
    }
    val animatedOffset by animateFloatAsState(
        targetValue = offsetY,
        label = "",
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    //TODO: Add shape animation (good for now)
    LazyColumnCategory(
        itemCount = itemCount,
        index = index,
        modifier = modifier
            .graphicsLayer {
                translationY = animatedOffset
            }
    ) { shape ->
        var elevated by remember { mutableStateOf(false) }
        val elevation by animateDpAsState(
            targetValue = if (elevated) 16.dp else 0.dp, label = ""
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = shape,
            color = CardDefaults.cardColors().containerColor,
            tonalElevation = elevation,
            shadowElevation = elevation
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "$sampler: ${stringResource(id = samplerNameId)}",
                )

                Icon(
                    imageVector = Icons.Filled.DragHandle,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                offsetY += delta
                                onEvent(UserEvent.MoveSampler((offsetY).roundToInt()))
                            },
                            onDragStarted = {
                                elevated = true
                                onEvent(UserEvent.StartMoveSampler(index, elementSize.roundToInt()))
                            },
                            onDragStopped = {
                                elevated = false
                                offsetY = 0f
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun Sliders(
    name: String,
    temperature: Float,
    topK: Int,
    topP: Float,
    typical: Float,
    minP: Float,
    topA: Float,
    tailFreeSampling: Float,
    repetitionPenalty: Float,
    repetitionPenaltyRange: Int,
    repetitionPenaltySlope: Float,
    mirostat: Int,
    mirostatTau: Float,
    mirostatEta: Float,
    onEvent: (UserEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name, onValueChange = {
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
            value = temperature,
            leftBorder = 0f,
            rightBorder = 4f,
            updateValue = { onEvent(UserEvent.EditTemperature(it)) },
            tooltipText = stringResource(R.string.temperature_tooltip)
        )

        TitledFiniteSlider(
            title = stringResource(R.string.top_k_title),
            value = topK,
            leftBorder = 0,
            rightBorder = 100,
            updateValue = { onEvent(UserEvent.EditTopK(it)) },
            tooltipText = stringResource(R.string.top_k_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.top_p_title),
            value = topP,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTopP(it)) },
            tooltipText = stringResource(R.string.top_p_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.typical_p_title),
            value = typical,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTypical(it)) },
            tooltipText = stringResource(R.string.typical_p_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.min_p_title),
            value = minP,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditMinP(it)) },
            tooltipText = stringResource(R.string.min_p_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.top_a_title),
            value = topA,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTopA(it)) },
            tooltipText = stringResource(R.string.top_a_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.tail_free_sampling_title),
            value = tailFreeSampling,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditTailFreeSampling(it)) },
            tooltipText = stringResource(R.string.tail_free_sampling_title_tooltip)
        )

        TitledFloatSlider(
            title = stringResource(R.string.repetition_penalty_title),
            value = repetitionPenalty,
            leftBorder = 1f,
            rightBorder = 3f,
            updateValue = { onEvent(UserEvent.EditRepetitionPenalty(it)) }
        )

        TitledFiniteSlider(
            title = stringResource(R.string.repetition_penalty_range_title),
            value = repetitionPenaltyRange,
            leftBorder = 0,
            rightBorder = 8192,
            updateValue = { onEvent(UserEvent.EditRepetitionPenaltyRange(it)) }
        )

        TitledFloatSlider(
            title = stringResource(R.string.repetition_penalty_slope_title),
            value = repetitionPenaltySlope,
            leftBorder = 0f,
            rightBorder = 10f,
            updateValue = { onEvent(UserEvent.EditRepetitionPenaltySlope(it)) }
        )

        HorizontalDivider()
        Text(
            text = "Mirostat",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        TitledFiniteSlider(
            title = "Mode",
            value = mirostat,
            leftBorder = 0,
            rightBorder = 2,
            updateValue = { onEvent(UserEvent.EditMirostatMode(it)) },
            tooltipText = "A value of 0 disables Mirostat entirely. 1 is for Mirostat 1.0, and 2 is for Mirostat 2.0"
        )

        TitledFloatSlider(
            title = "Tau",
            value = mirostatTau,
            leftBorder = 0f,
            rightBorder = 20f,
            updateValue = { onEvent(UserEvent.EditMirostatTau(it)) },
            tooltipText = "Controls variability of Mirostat outputs"
        )

        TitledFloatSlider(
            title = "Eta",
            value = mirostatEta,
            leftBorder = 0f,
            rightBorder = 1f,
            updateValue = { onEvent(UserEvent.EditMirostatEta(it)) },
            tooltipText = "Controls learning rate of Mirostat"
        )
    }
}

@Composable
fun LazyColumnCategory(
    itemCount: Int,
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Shape) -> Unit
) {
    val default = MaterialTheme.shapes.medium
    Column(modifier = modifier) {
        val shape = when {
            itemCount == 1 -> default
            index == 0 -> default.copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
            index == itemCount - 1 -> default.copy(
                topEnd = CornerSize(0.dp),
                topStart = CornerSize(0.dp)
            )

            else -> RoundedCornerShape(0.dp)
        }

        content(shape)

        if (index != itemCount - 1) {
            HorizontalDivider()
        }
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