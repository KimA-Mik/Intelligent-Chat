package ru.kima.intelligentchat.presentation.cardDetails

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardDetailContent(
    state: CardDetailsState,
    modifier: Modifier = Modifier,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Row(modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)) {
                CardImage(
                    state.card.photoBytes,
                    modifier = Modifier.size(100.dp)
                ) {
                    onEvent(CardDetailUserEvent.SelectImageClicked)
                }
                Text(
                    text = state.card.name,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f)
                )
            }
        }

        item {
            Text(
                text = state.card.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
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
            val card = ru.kima.intelligentchat.domain.card.model.CharacterCard(
                name = "Name",
                description = "Description"
            )
            CardDetailContent(CardDetailsState(card)) {

            }
        }
    }
}