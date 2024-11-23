package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.AsyncCardImage
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.CardDetailsDefaults
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.CardField
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.common.components.clearFocusOnSoftKeyboardHide
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

private val tabs: ImmutableList<Int> = persistentListOf(
    R.string.tab_item_history,
    R.string.tab_item_system,
    R.string.tab_item_stats,
)


@Composable
fun CardDetailContent(
    state: CardDetailsState,
    modifier: Modifier = Modifier,
    onEvent: (CardDetailUserEvent) -> Unit
) = Column(
    modifier = modifier,
) {
    val pagerState = rememberPagerState { tabs.size }

    val scope = rememberCoroutineScope()
    LaunchedEffect(state.selectedTabIndex) {
        scope.launch { pagerState.animateScrollToPage(state.selectedTabIndex) }
    }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(CardDetailUserEvent.SelectTab(pagerState.currentPage))
    }
    TabRow(selectedTabIndex = state.selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = state.selectedTabIndex == index,
                onClick = { onEvent(CardDetailUserEvent.SelectTab(index)) },
                text = {
                    Text(
                        text = stringResource(title),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    }

    HorizontalPager(pagerState) {
        val innerModifier = Modifier.fillMaxSize()
        when (it) {
            0 -> HistoryTab(
                tokensCount = state.tokensCount,
                description = state.card.description,
                descriptionExpanded = state.switchesState.description,
                firstMes = state.card.firstMes,
                firstMesExpanded = state.switchesState.firstMes,
                personality = state.card.personality,
                personalityExpanded = state.switchesState.personality,
                scenario = state.card.scenario,
                scenarioExpanded = state.switchesState.scenario,
                exampleDialog = state.card.mesExample,
                exampleDialogExpanded = state.switchesState.mesExample,
                modifier = innerModifier,
                onEvent = onEvent,
            )

            1 -> SystemTab(
                modifier = innerModifier,
            )

            2 -> StatsTab(
                modifier = innerModifier,
            )

            else -> UnreachableTab(
                modifier = innerModifier,
            )
        }
    }
}


@Composable
fun HistoryTab(
    tokensCount: CardDetailsState.TokensCount,
    description: String,
    descriptionExpanded: Boolean,
    firstMes: String,
    firstMesExpanded: Boolean,
    personality: String,
    personalityExpanded: Boolean,
    scenario: String,
    scenarioExpanded: Boolean,
    exampleDialog: String,
    exampleDialogExpanded: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GeneralInfo(
            text = description,
            title = stringResource(R.string.card_description_title),
            field = CardField.Description,
            isExpanded = descriptionExpanded,
            modifier = Modifier.padding(8.dp),
            textTokensCount = tokensCount.description,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = firstMes,
            title = stringResource(R.string.card_first_message_title),
            field = CardField.FirstMes,
            isExpanded = firstMesExpanded,
            modifier = Modifier.padding(8.dp),
            textTokensCount = tokensCount.firstMes,
            showTokensCount = true,
            supportRow = {
                TextButton(onClick = {
                    onEvent(CardDetailUserEvent.OpenAltGreetingsSheet)
                }) {
                    Text(text = stringResource(R.string.alternate_greetings_button))
                }
            },
            onEvent = onEvent
        )

        GeneralInfo(
            text = personality,
            title = stringResource(R.string.card_personality_title),
            field = CardField.Personality,
            isExpanded = personalityExpanded,
            modifier = Modifier.padding(8.dp),
            textTokensCount = tokensCount.personality,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = scenario,
            title = stringResource(R.string.card_scenario_title),
            field = CardField.Scenario,
            isExpanded = scenarioExpanded,
            modifier = Modifier.padding(8.dp),
            textTokensCount = tokensCount.scenario,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = exampleDialog,
            title = stringResource(R.string.card_example_dialogs_title),
            field = CardField.CreatorNotes,
            isExpanded = exampleDialogExpanded,
            modifier = Modifier.padding(8.dp),
            textTokensCount = tokensCount.mesExample,
            showTokensCount = true,
            onEvent = onEvent
        )
    }
}

@Composable
fun SystemTab(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "(⌒_⌒;)\nHere will be system fields soon",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatsTab(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "(„ಡωಡ„)\nHere will be stats soon",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UnreachableTab(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier.verticalScroll(scrollState),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "୧((#Φ益Φ#))୨", style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun HeadArea(
    name: String,
    cardTokens: Int,
    nameTokensCount: Int,
    photoName: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = CardDetailsDefaults.imageSize,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncCardImage(
            // FIXME: Fix later
            photoName = photoName,
            imageSize = imageSize
        ) {
            onEvent(CardDetailUserEvent.SelectImageClicked)
        }


        OutlinedTextField(
            label = {
                Text(text = stringResource(R.string.card_name_title))
            },
            value = name, onValueChange = { newValue ->
                onEvent(
                    CardDetailUserEvent.FieldUpdate(
                        CardField.Name,
                        newValue
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clearFocusOnSoftKeyboardHide(),
            maxLines = 2,
            textStyle = MaterialTheme.typography.bodyLarge,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Card tokens: $cardTokens",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (cardTokens < 2048)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.error
                    )
                    TokensCountText(tokens = nameTokensCount)
                }
            }
        )
    }
}

@Composable
fun GeneralInfo(
    text: String,
    title: String,
    field: CardField,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    textTokensCount: Int = 0,
    showTokensCount: Boolean = false,
    supportRow: @Composable RowScope.() -> Unit = { Spacer(Modifier.weight(1f)) },
    onEvent: (CardDetailUserEvent) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Column(modifier) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    onEvent(CardDetailUserEvent.FieldSwitch(field))
                }
            ) {
                Icon(
                    modifier = Modifier.graphicsLayer(
                        rotationZ = rotation
                    ),
                    imageVector = Icons.Filled.ArrowDropUp,
                    contentDescription = ""
                )
            }
        }

        AnimatedVisibility(isExpanded) {

            OutlinedTextField(
                value = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clearFocusOnSoftKeyboardHide(),
                onValueChange = { updated ->
                    onEvent(CardDetailUserEvent.FieldUpdate(field, updated))
                },
                supportingText = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        supportRow()

                        if (showTokensCount) {
                            TokensCountText(textTokensCount)
                        }
                    }
                })
        }
    }
}

@Composable
fun TokensCountText(
    tokens: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.tokens_count, tokens),
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall
    )
}

@Preview(name = "Card details light mode")
@Preview(
    name = "Card details dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewCardDetails() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val card = ImmutableCard(
                name = "Name",
                description = "Description"
            )
            CardDetailContent(CardDetailsState(card)) {

            }
        }
    }
}