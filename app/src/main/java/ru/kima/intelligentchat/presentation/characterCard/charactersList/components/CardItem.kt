package ru.kima.intelligentchat.presentation.characterCard.charactersList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardItem(
    card: CardEntry,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onCardClick() },
    ) {
        Row {
            CardImage(
                photoBytes = card.photoBytes,
                modifier = Modifier
                    .size(72.dp)
                    .padding(8.dp),
                onClick = onAvatarClick
            )

            Text(
                text = card.name,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = card.characterVersion,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = card.creatorNotes,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun PreviewCardItem() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CardItem(
                card = CardEntry(
                    id = 0,
                    name = "Name",
                    creatorNotes = "This is a character",
                    characterVersion = "main"
                ),
                modifier = Modifier.padding(8.dp),
                onAvatarClick = {},
                onCardClick = {}
            )
        }
    }
}