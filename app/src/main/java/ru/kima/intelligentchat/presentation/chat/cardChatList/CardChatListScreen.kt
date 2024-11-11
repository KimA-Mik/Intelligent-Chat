package ru.kima.intelligentchat.presentation.chat.cardChatList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UiEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.model.ChatListItem
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardChatListScreen(
    state: CardChatListState,
    uiEvent: Event<UiEvent>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onEvent: (UserEvent) -> Unit
) {
    val popBackStack = remember<() -> Unit> {
        {
            navController.popBackStack()
        }
    }

    when {
        state.deleteChatDialog -> SimpleAlertDialog(
            onConfirm = { onEvent(UserEvent.DeleteChatAccept) },
            onDismiss = { onEvent(UserEvent.DeleteChatDismiss) },
            title = stringResource(R.string.delete_chat_alert_dialog_title),
            text = stringResource(R.string.delete_chat_alert_dialog_text),
            icon = Icons.Default.DeleteForever,
            confirmText = stringResource(R.string.alert_dialog_accept),
            dismissText = stringResource(R.string.alert_dialog_dismiss)
        )

        state.renameChatDialog -> AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = { onEvent(UserEvent.RenameChatAccept) }) {
                    Text(stringResource(R.string.alert_dialog_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(UserEvent.RenameChatDismiss) }) {
                    Text(stringResource(R.string.alert_dialog_dismiss))
                }
            },
            title = {
                Text(stringResource(R.string.rename_chat_dialog_title))
            },
            text = {
                OutlinedTextField(
                    value = state.renameChatBuffer,
                    onValueChange = { onEvent(UserEvent.RenameChatUpdateBuffer(it)) }
                )
            },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.displayCard.image.imageBitmap?.let {
                            CardImage(
                                bitmap = state.displayCard.image,
                                modifier = Modifier.size(40.dp),
                                onClick = {}
                            )
                        }
                        Text(
                            text = state.displayCard.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(UserEvent.CreateChat) }
            ) {
                Icon(Icons.Default.Create, contentDescription = null)
            }
        }
    ) { paddingValues ->
        CardChatListScreenContent(
            chats = state.chats,
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent
        )
    }
}

@Composable
fun CardChatListScreenContent(
    chats: List<ChatListItem>,
    modifier: Modifier = Modifier,
    onEvent: (UserEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = chats,
            key = { it.id }) {
            ListItem(
                headlineContent = { Text(it.name) },
                modifier = Modifier
//                    .clickable {
//                        onEvent(UserEvent.SelectChat(it.id))
//                    }
                    .animateItem(),
                trailingContent = {
                    SimpleDropdownMenu(
                        listDropdownMenuItems(
                            chatId = it.id,
                            onEvent = onEvent
                        )
                    )
                },
                tonalElevation = if (it.selected) 4.dp else ListItemDefaults.Elevation,
                shadowElevation = if (it.selected) 4.dp else ListItemDefaults.Elevation,
            )
        }
    }
}

@Composable
private fun listDropdownMenuItems(
    chatId: Long,
    onEvent: (UserEvent) -> Unit
) = remember {
    listOf(
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_select_chat,
            onClick = { onEvent(UserEvent.SelectChat(chatId)) },
            iconVector = Icons.Default.Check
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_rename_chat,
            onClick = { onEvent(UserEvent.RenameChat(chatId)) },
            iconVector = Icons.Default.EditNote
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_delete_chat,
            onClick = { onEvent(UserEvent.DeleteChat(chatId)) },
            iconVector = Icons.Default.Delete
        )
    )
}

@Preview
@Composable
private fun CardChatListScreenPreview() {
    IntelligentChatTheme {
        CardChatListScreen(
            state = CardChatListState(
                displayCard = DisplayCard(name = "Card"),
                chats = List(10) {
                    ChatListItem(
                        id = it.toLong(),
                        name = it.toString(),
                        selected = it == 1,
                    )
                }
            ),
            uiEvent = Event(null),
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            modifier = Modifier.fillMaxSize(),
            onEvent = {}
        )
    }
}