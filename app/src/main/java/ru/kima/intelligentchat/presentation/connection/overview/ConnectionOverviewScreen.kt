package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionOverviewScreen(
    state: ConnectionOverviewState,
    drawerState: DrawerState,
    onEvent: (COUserEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.nav_item_connection),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This is a connection screen")
        }
    }
}

@Preview
@Composable
fun ConnectionOverviewPreview() {
    IntelligentChatTheme {
        Surface(Modifier.fillMaxSize()) {
            ConnectionOverviewScreen(
                state = ConnectionOverviewState(),
                drawerState = DrawerState(initialValue = DrawerValue.Closed),
                onEvent = {}
            )
        }
    }
}