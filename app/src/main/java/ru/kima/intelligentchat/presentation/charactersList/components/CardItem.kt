package ru.kima.intelligentchat.presentation.charactersList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardItem(
    card: CharacterCard,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
            )
            .then(modifier)
    ) {
        Text(
            text = card.name,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            text = card.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
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
            )
        }
    }
}