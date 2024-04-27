package ru.kima.intelligentchat.presentation.characterCard.charactersList

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.charactersList.components.CardItem
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.personas.common.PersonaImage
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersListContent(
    modifier: Modifier = Modifier,
    state: CharactersListState,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    Box(modifier) {
        SearchField(state.searchText, state.personaImage, onEvent)
        CharactersList(state.cards, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    query: String,
    personaImage: PersonaImageContainer,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    SearchBar(
        query = query,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(56.dp),
        onQueryChange = { newQuery ->
            onEvent(CharactersListUserEvent.SearchQueryChanged(newQuery))
        },
        onSearch = {},
        active = false,
        placeholder = {
            Text(text = "Search")
        },
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { onEvent(CharactersListUserEvent.OnMenuButtonClicked) }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Open menu")
            }
        },
        trailingIcon = {
            PersonaImage(
                container = personaImage,
                onClick = {},
                border = false,
                modifier = Modifier.size(30.dp)
            )
        },
        content = {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharactersList(
    cards: List<ImmutableCardEntry>,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(top = 52.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 32.dp, bottom = 16.dp)
    ) {
        items(cards, key = { item -> item.id }) { card ->
            //TODO: decode image from bytes cause lags
            CardItem(
                card = card,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .animateItemPlacement(),
                dropDownMenuItems = itemDropDownMenuItems(id = card.id, onEvent = onEvent),
                onAvatarClick = {
                    onEvent(CharactersListUserEvent.ShowCardAvatar(card.id))
                },
                onCardClick = {
                    onEvent(CharactersListUserEvent.OpenCardChat(card.id))
                })
        }
    }
}

@Composable
private fun itemDropDownMenuItems(
    id: Long,
    onEvent: (CharactersListUserEvent) -> Unit
) = remember {
    listOf(
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_edit_card,
            onClick = { onEvent(CharactersListUserEvent.EditCardClicked(id)) },
            iconVector = Icons.Default.Create
        )
    )
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
                ImmutableCardEntry(
                    id = index.toLong(),
                    name = "Name $index",
                    characterVersion = "Version $index",
                    creatorNotes = "Notes $index"
                )
            }
            CharactersListContent(state = CharactersListState(cards)) {

            }
        }
    }
}