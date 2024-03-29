package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap
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

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeadArea(
            name = state.card.name,
            nameTokensCount = state.nameTokensCount,
            photo = state.card.photoBytes,
            onEvent = onEvent
        )

        GeneralInfo(
            text = state.card.description,
            textTokensCount = state.descriptionTokensCount,
            modifier = Modifier.padding(8.dp),
            title = "Description",
            field = CardDetailsViewModel.CardField.Description,
            isExpanded = isDescriptionExpanded,
            onEvent = onEvent,
            onExpand = { isDescriptionExpanded = !isDescriptionExpanded }
        )

        GeneralInfo(
            text = state.card.firstMes,
            textTokensCount = state.firstMesTokensCount,
            modifier = Modifier.padding(8.dp),
            title = "First message",
            field = CardDetailsViewModel.CardField.FirstMes,
            isExpanded = isFirstMesExpanded,
            onEvent = onEvent,
            supportRow = {
                TextButton(onClick = {
                    onEvent(CardDetailUserEvent.OpenAltGreetingsSheet)
                }) {
                    Text(text = "Alternative greetings")
                }
            },
            onExpand = { isFirstMesExpanded = !isFirstMesExpanded }
        )

        GeneralInfo(
            text = state.card.personality,
            textTokensCount = state.personalityTokensCount,
            modifier = Modifier.padding(8.dp),
            title = "Personality",
            field = CardDetailsViewModel.CardField.Personality,
            isExpanded = isPersonalityExpanded,
            onEvent = onEvent,
            onExpand = { isPersonalityExpanded = !isPersonalityExpanded }
        )

        GeneralInfo(
            text = state.card.scenario,
            textTokensCount = state.scenarioTokensCount,
            modifier = Modifier.padding(8.dp),
            title = "Scenario",
            field = CardDetailsViewModel.CardField.Scenario,
            isExpanded = isScenarioExpanded,
            onEvent = onEvent,
            onExpand = { isScenarioExpanded = !isScenarioExpanded }
        )
    }
}

@Composable
fun HeadArea(
    name: String,
    nameTokensCount: Int,
    photo: Bitmap?,
    modifier: Modifier = Modifier,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            .then(modifier)
    ) {
        CardImage(
            // FIXME: Fix later
            ImmutableBitmap(photo),
            modifier = Modifier.size(100.dp)
        ) {
            onEvent(CardDetailUserEvent.SelectImageClicked)
        }


        OutlinedTextField(
            label = {
                Text(text = "Name")
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
                TokensCountText(tokens = nameTokensCount)
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
    showTokensCount: Boolean = true,
    supportRow: @Composable () -> Unit = {},
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
                        horizontalArrangement = Arrangement.End,
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
fun TokensCountText(tokens: Int) {
    Text(
        text = stringResource(id = R.string.tokens_count, tokens),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
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