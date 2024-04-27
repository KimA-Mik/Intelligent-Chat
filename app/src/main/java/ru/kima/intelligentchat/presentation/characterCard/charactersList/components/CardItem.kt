package ru.kima.intelligentchat.presentation.characterCard.charactersList.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardItem(
    card: ImmutableCardEntry,
    modifier: Modifier = Modifier,
    dropDownMenuItems: List<SimpleDropDownMenuItem> = emptyList(),
    onAvatarClick: () -> Unit,
    onCardClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.clickable { onCardClick() },
    ) {
        Row {
            CardImage(
                image = card.thumbnail,
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
            Column(horizontalAlignment = Alignment.End) {

                Text(
                    text = card.characterVersion,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (dropDownMenuItems.isNotEmpty()) {
                    SimpleDropdownMenu(menuItems = dropDownMenuItems)
                }
            }
        }
        Text(
            text = card.creatorNotes,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .animateContentSize()
                .clickable { isExpanded = !isExpanded },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 2
        )
    }
}

@Preview
@Composable
fun PreviewCardItem() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CardItem(
                card = ImmutableCardEntry(
                    id = 0,
                    name = "Name",
                    creatorNotes = "This is a character",
                    characterVersion = "main"
                ),
                modifier = Modifier.padding(8.dp),
                dropDownMenuItems = listOf(
                    SimpleDropDownMenuItem(
                        textId = R.string.menu_item_edit_card,
                        onClick = {},
                        iconVector = Icons.Default.Create
                    )
                ),
                onAvatarClick = {},
                onCardClick = {}
            )
        }
    }
}