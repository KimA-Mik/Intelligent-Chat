package ru.kima.intelligentchat.presentation.characterCard.charactersList

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.charactersList.components.CardItem
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersList(
    cards: ImmutableList<ImmutableCardEntry>,
    modifier: Modifier = Modifier,
    onEvent: (CharactersListUserEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
        contentPadding = PaddingValues(top = 32.dp, bottom = 16.dp)
    ) {
        items(cards, key = { item -> item.id }) { card ->
            //TODO: decode image from bytes cause lags
            CardItem(
                card = card,
                modifier = Modifier.animateItem(),
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
    persistentListOf(
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_edit_card,
            onClick = { onEvent(CharactersListUserEvent.EditCardClicked(id)) },
            iconVector = Icons.Default.Create
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_delete_card,
            onClick = { onEvent(CharactersListUserEvent.DeleteCardClicked(id)) },
            iconVector = Icons.Default.Delete
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
            }.toImmutableList()
            CharactersList(
                cards = cards,
                onEvent = {}
            )
        }
    }
}

@Preview(
    name = "Characters List Preview expanded",
    widthDp = 600
)
@Composable
fun CharactersListPreviewExpanded() {
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
            }.toImmutableList()
            CharactersList(
                cards = cards,
                onEvent = {}
            )
        }
    }
}