package ru.kima.intelligentchat.presentation.charactersList

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.charactersList.components.CardItem
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersListContent(
    state: CharactersListState,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        items(state.cards) { card ->
            CardItem(
                card = card,
                modifier = Modifier.padding()
            ) {
                onEvent(CharactersListUserEvent.CardSelected(card.id))
            }
        }
    }
}

@Preview(name = "Characters List Preview light theme")
@Preview(
    name = "Characters List Preview dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CharactersListPreview() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val cards = List(100) { index ->
                ru.kima.intelligentchat.domain.card.model.CharacterCard(
                    name = "Name $index",
                    description = "Description $index"
                )
            }
            CharactersListContent(CharactersListState(cards)) {

            }
        }
    }

}