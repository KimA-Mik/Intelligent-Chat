package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.RememberMe
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    state: ChatScreenState,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    onEvent: (UserEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Persona ${state.selectedPersona}")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Navigation menu"
                        )
                    }
                },
                actions = {
                    SimpleDropdownMenu(
                        menuItems = dropdownMenuItems(),
                        iconVector = Icons.Default.MoreVert
                    )
                },
                scrollBehavior = remember { scrollBehavior }
            )
        },
        bottomBar = {
            ChatBottomBar(
                value = state.inputMessageBuffer,
                onValueChange = { onEvent(UserEvent.UpdateInputMessage(it)) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ChatScreenContent(
            state = state,
            modifier = Modifier
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
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
fun ChatBottomBar(
    value: String,
    onValueChange: (String) -> Unit
) {

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
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Send,
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
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .heightIn(min = 56.dp)
            .padding(8.dp),
        textStyle = MaterialTheme.typography.bodyLarge
    ) { innerTextField ->
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = color,
            contentColor = MaterialTheme.colorScheme.contentColorFor(color)
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


@Composable
fun ChatScreenContent(
    state: ChatScreenState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Now selected persona is ${state.selectedPersona}")
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    IntelligentChatTheme {
        ChatScreen(
            state = ChatScreenState(),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}