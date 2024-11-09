package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableAltGreeting
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun AltGreetingsSheetContent(
    greetings: List<ImmutableAltGreeting>,
    editableGreeting: Long,
    editableGreetingBuffer: String,
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

        itemsIndexed(
            items = greetings,
            key = { _, item -> item.id }
        ) { index, greeting ->
            val edited = editableGreeting == greeting.id
            AnimatedContent(
                targetState = edited, label = "",
                modifier = Modifier.animateItem(),
            ) { editable ->
                if (editable) {
                    EditableAlternateGreeting(
                        buffer = editableGreetingBuffer,
                        position = index,
                        onEvent = onEvent
                    )
                } else {
                    AlternateGreeting(
                        greeting = greeting,
                        position = index,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun AlternateGreeting(
    greeting: ImmutableAltGreeting,
    position: Int,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Greeting #${position + 1}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEvent(CardDetailUserEvent.EditAltGreeting(greeting.id)) }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
            }
            IconButton(onClick = { onEvent(CardDetailUserEvent.DeleteAltGreeting(greeting.id)) }) {
                Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = null)
            }
        }

        Text(
            text = greeting.body,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        )
    }
}

@Composable
fun EditableAlternateGreeting(
    buffer: String,
    position: Int,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Greeting #${position + 1}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onEvent(CardDetailUserEvent.AcceptAltGreetingEdit) }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = null)
            }
            IconButton(onClick = { onEvent(CardDetailUserEvent.RejectAltGreetingEdit) }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
        }

        OutlinedTextField(
            value = buffer,
            onValueChange = { onEvent(CardDetailUserEvent.UpdateAlternateGreetingBuffer(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview
@Composable
fun AltGreetingsSheetPreview() {
    IntelligentChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AltGreetingsSheetContent(
                greetings = List(10) {
                    ImmutableAltGreeting(
                        id = it.toLong(),
                        body = "Greeting $it"
                    )
                },
                editableGreeting = 0L,
                editableGreetingBuffer = "Buffer",
                onEvent = {}
            )
        }
    }
}