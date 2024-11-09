package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.RememberMe
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.chat.chatScreen.components.ChatMessage
import ru.kima.intelligentchat.presentation.chat.chatScreen.components.EditableChatMessage
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableMessagingIndicator
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenContent(
    state: ChatScreenState.ChatState,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onEvent: (UserEvent) -> Unit
) {
    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val sb = remember { scrollBehavior }
    val popBackStack = remember<() -> Unit> {
        {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.info.characterCard.image.imageBitmap?.let {
                            CardImage(
                                bitmap = state.info.characterCard.image,
                                modifier = Modifier.size(40.dp),
                                onClick = {}
                            )
                        }
                        Text(
                            text = state.info.characterCard.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2
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
                actions = {
                    SimpleDropdownMenu(
                        menuItems = dropdownMenuItems(),
                        iconVector = Icons.Default.MoreVert
                    )
                },
                scrollBehavior = sb
            )
        },
        bottomBar = {
            ChatBottomBar(
                value = state.inputMessageBuffer,
                messagingIndicator = state.status,
                onMessageButtonClicked = { onEvent(UserEvent.MessageButtonClicked) },
                onValueChange = { onEvent(UserEvent.UpdateInputMessage(it)) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.info.fullChat.messages.isNotEmpty()) {
            Messages(
                state = state.info,
                editMessageBuffer = state.editMessageBuffer,
                editMessageId = state.editMessageId,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun dropdownMenuItems() = remember {
    listOf(
        SimpleDropDownMenuItem(
            R.string.menu_item_chat_instances,
            onClick = {},
            iconVector = Icons.Default.Feedback
        ),
    )
}

@Composable
private fun moreMenuItems() = remember {
    listOf(
        SimpleDropDownMenuItem(
            R.string.menu_item_impersonate,
            onClick = {},
            iconVector = Icons.Default.RememberMe
        ),
        SimpleDropDownMenuItem(
            R.string.menu_item_count_tokens,
            onClick = {},
            iconVector = Icons.Default.Numbers
        )
    )
}


@Composable
fun MessageIndicator(
    indicator: ImmutableMessagingIndicator,
    modifier: Modifier = Modifier
) {
    when (indicator) {
        is ImmutableMessagingIndicator.DeterminedGenerating -> LinearProgressIndicator(
            progress = { indicator.progress },
            modifier
        )

        ImmutableMessagingIndicator.Generating -> LinearProgressIndicator(modifier)
        ImmutableMessagingIndicator.None -> {}
        ImmutableMessagingIndicator.Pending -> LinearProgressIndicator(progress = { 0f }, modifier)
    }
}


@Composable
fun Messages(
    state: ChatScreenState.ChatState.ChatInfo,
    editMessageBuffer: String,
    editMessageId: Long,
    modifier: Modifier,
    onEvent: (UserEvent) -> Unit
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.fullChat.messages.lastIndex
    )

    LaunchedEffect(key1 = state.fullChat.messages) {
        val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@LaunchedEffect

        listState.scrollToItem(
            state.fullChat.messages.lastIndex,
            scrollOffset = lastItem.size
        )
    }

    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.fullChat.messages,
            key = { it.messageId }) {
            val edited = it.messageId == editMessageId
//            AnimatedContent(
//                targetState = it.messageId == editMessageId, label = "",
//                modifier = Modifier.animateItem()
//            ) { edited ->
            when (edited) {
                true -> EditableChatMessage(
                    message = it,
                    buffer = editMessageBuffer,
                    onType = { text -> onEvent(UserEvent.UpdateEditedMessage(text)) },
                    onSaveClick = { onEvent(UserEvent.SaveEditedMessage) },
                    onDismissClick = { onEvent(UserEvent.DismissEditedMessage) },
                    onImageClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .animateItem(),
                )

                false -> ChatMessage(
                    message = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .animateContentSize { _, targetValue ->
                            scope.launch {
                                listState.animateScrollToItem(
                                    it.index,
                                    scrollOffset = targetValue.height
                                )
                            }
                        }
                        .animateItem(),
                    onImageClicked = {},
                    onLeftClicked = { onEvent(UserEvent.MessageSwipeLeft(it.messageId)) },
                    onRightClicked = { onEvent(UserEvent.MessageSwipeRight(it.messageId)) },
                    onEditClicked = { onEvent(UserEvent.EditMessage(it.messageId)) },
                    onDeleteClicked = { onEvent(UserEvent.DeleteMessage(it.messageId)) },
                    onMoveUpClicked = { onEvent(UserEvent.MoveMessageUp(it.messageId)) },
                    onMoveDownClicked = { onEvent(UserEvent.MoveMessageDown(it.messageId)) }
                )
//                }
            }
        }
    }
}

@Composable
fun ChatBottomBar(
    value: String,
    messagingIndicator: ImmutableMessagingIndicator,
    modifier: Modifier = Modifier,
    onMessageButtonClicked: () -> Unit,
    onValueChange: (String) -> Unit
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.TopCenter
) {
    MessageIndicator(
        indicator = messagingIndicator,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleDropdownMenu(
            menuItems = moreMenuItems(),
            iconVector = Icons.Default.MoreHoriz
        )

        ChatTextField(
            value = value,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(R.string.placeholder_enter_your_message),
            onValueChange = onValueChange
        )
        IconButton(onClick = onMessageButtonClicked) {
            Icon(
                imageVector =
                if (messagingIndicator == ImmutableMessagingIndicator.None) Icons.AutoMirrored.Default.Send
                else Icons.Default.StopCircle,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ChatTextField(
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String = String(),
    onValueChange: (String) -> Unit
) {
    val color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val contentColor = contentColorFor(color)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .heightIn(min = 56.dp)
            .padding(8.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
        cursorBrush = SolidColor(contentColor)
    ) { innerTextField ->
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = color,
        ) {
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                innerTextField()
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    IntelligentChatTheme {
        ChatScreenContent(
            state = ChatScreenState.ChatState(
                info = ChatScreenState.ChatState.ChatInfo(
                    fullChat = DisplayChat(
                        messages = listOf(
                            DisplayMessage(
                                messageId = 0,
                                senderName = "Sender",
                                text = "Message Text",
                            ), DisplayMessage(
                                messageId = 1,
                                senderName = "Sender",
                                text = "Message Text",
                                senderType = SenderType.Persona
                            ), DisplayMessage(
                                messageId = 2,
                                senderName = "Sender",
                                text = "Message Text",
                                senderType = SenderType.Character,
                                showSwipeInfo = true
                            )
                        )
                    )
                )
            ),
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}