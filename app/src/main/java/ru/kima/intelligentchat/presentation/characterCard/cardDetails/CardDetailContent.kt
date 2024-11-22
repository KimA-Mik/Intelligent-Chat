package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.AsyncCardImage
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardDetailContent(
    state: CardDetailsState,
    modifier: Modifier = Modifier,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    var isDescriptionExpanded by remember { mutableStateOf(true) }
    var isFirstMesExpanded by remember { mutableStateOf(true) }
    var isPersonalityExpanded by remember { mutableStateOf(false) }
    var isScenarioExpanded by remember { mutableStateOf(false) }
    var isCreatorsNotesExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeadArea(
            name = state.card.name,
            cardTokens = state.tokensCount.totalTokens,
            nameTokensCount = state.tokensCount.name,
            photoName = state.card.photoName,
            onEvent = onEvent
        )

        GeneralInfo(
            text = state.card.description,
            title = stringResource(R.string.card_description_title),
            field = CardDetailsViewModel.CardField.Description,
            isExpanded = isDescriptionExpanded,
            onExpand = { isDescriptionExpanded = !isDescriptionExpanded },
            modifier = Modifier.padding(8.dp),
            textTokensCount = state.tokensCount.description,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = state.card.firstMes,
            title = stringResource(R.string.card_first_message_title),
            field = CardDetailsViewModel.CardField.FirstMes,
            isExpanded = isFirstMesExpanded,
            onExpand = { isFirstMesExpanded = !isFirstMesExpanded },
            modifier = Modifier.padding(8.dp),
            textTokensCount = state.tokensCount.firstMes,
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
            text = state.card.personality,
            title = stringResource(R.string.card_personality_title),
            field = CardDetailsViewModel.CardField.Personality,
            isExpanded = isPersonalityExpanded,
            onExpand = { isPersonalityExpanded = !isPersonalityExpanded },
            modifier = Modifier.padding(8.dp),
            textTokensCount = state.tokensCount.personality,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = state.card.scenario,
            title = stringResource(R.string.card_scenario_title),
            field = CardDetailsViewModel.CardField.Scenario,
            isExpanded = isScenarioExpanded,
            onExpand = { isScenarioExpanded = !isScenarioExpanded },
            modifier = Modifier.padding(8.dp),
            textTokensCount = state.tokensCount.scenario,
            showTokensCount = true,
            onEvent = onEvent
        )

        GeneralInfo(
            text = state.card.creatorNotes,
            title = stringResource(R.string.card_creators_note_title),
            field = CardDetailsViewModel.CardField.CreatorNotes,
            isExpanded = isCreatorsNotesExpanded,
            onExpand = { isCreatorsNotesExpanded = !isCreatorsNotesExpanded },
            modifier = Modifier.padding(8.dp),
            onEvent = onEvent
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
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            .then(modifier)
    ) {
        AsyncCardImage(
            // FIXME: Fix later
            photoName = photoName,
            imageSize = 100.dp
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
                        CardDetailsViewModel.CardField.Name,
                        newValue
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .weight(1f),
            maxLines = 3,
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
    field: CardDetailsViewModel.CardField,
    isExpanded: Boolean,
    onExpand: () -> Unit,
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
                onClick = onExpand
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
                    .padding(8.dp),
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