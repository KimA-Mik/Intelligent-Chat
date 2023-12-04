package ru.kima.intelligentchat.presentation.charactersList

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.charactersList.components.CardItem
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersListContent(
    state: CharactersListState,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    BoxWithConstraints {
        SearchField(state.searchText, onEvent)
        CharactersList(state.cards, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    query: String,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    SearchBar(
        query = query,
        modifier = Modifier
            .padding(8.dp)
            .height(56.dp),
        onQueryChange = { newQuery ->
            onEvent(CharactersListUserEvent.SearchQueryChanged(newQuery))
        },
        onSearch = {},
        active = false,
        placeholder = {
            Text(text = "Search")
        },
        onActiveChange = {}) {

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharactersList(
    cards: List<CharacterCard>,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 36.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 44.dp, bottom = 16.dp)
    ) {
        items(cards, key = { item -> item.id }) { card ->
            //TODO: decode image from bytes cause lags
            CardItem(
                card = card,
                modifier = Modifier
                    .padding()
                    .animateItemPlacement(),
                onAvatarClick = {
                    onEvent(CharactersListUserEvent.ShowCardAvatar(card.id))
                },
                onCardClick = {
                    onEvent(CharactersListUserEvent.CardSelected(card.id))
                })
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
                CharacterCard(
                    id = index.toLong(),
                    name = "Name $index",
                    description = "Description $index"
                )
            }
            CharactersListContent(CharactersListState(cards)) {

            }
        }
    }

}