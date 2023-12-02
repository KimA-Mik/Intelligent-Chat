package ru.kima.intelligentchat.presentation.charactersList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardItem(
    card: CharacterCard,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.clickable { onClick() }
        ) {
            Row {
                CardImage(
                    photoBytes = card.photoBytes,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(8.dp)
                ) {

                }

                Text(
                    text = card.name,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }
            Text(
                text = card.creatorNotes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewCardItem() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CardItem(
                card = CharacterCard(
                    name = "Name",
                    description = "Description"
                ),
                modifier = Modifier.padding(8.dp)
            ) {

            }
        }
    }
}