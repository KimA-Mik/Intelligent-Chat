package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun AltGreetingsSheetContent(
    greetings: List<AltGreeting>,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(text = "Alternate Greetings", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { onEvent(CardDetailUserEvent.CreateAltGreeting) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }
        }

        items(
            count = greetings.size,
            key = { greetings[it].id }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Greeting #${it + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = null)
                }
            }

            Text(
                text = greetings[it].body,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )
        }
    }
}


@Preview
@Composable
fun AltGreetingsSheetPreview() {
    IntelligentChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AltGreetingsSheetContent(
                greetings = List(10) { AltGreeting(id = it.toLong(), body = "Greeting $it") },
                onEvent = {}
            )
        }
    }
}