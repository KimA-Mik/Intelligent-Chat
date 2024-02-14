package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.overview.fragments.HordeFragment
import ru.kima.intelligentchat.presentation.connection.overview.fragments.KoboldAiFragment
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionOverviewScreen(
    state: ConnectionOverviewState,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ConnectionOverviewContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionOverviewContent(
    state: ConnectionOverviewState,
    modifier: Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    var isApiMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        ExposedDropdownMenuBox(
            expanded = isApiMenuExpanded,
            onExpandedChange = {
                isApiMenuExpanded = it
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (isApiMenuExpanded) 0f else 180f,
                label = "rotation"
            )

            TextField(
                value = stringResource(id = apiTypeStringResource(state.selectedApiType)),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropUp, contentDescription = "",
                            modifier = Modifier.graphicsLayer(
                                rotationZ = rotation
                            )
                        )
                    }
                },
                label = { Text(text = stringResource(R.string.apiTypeLabel)) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isApiMenuExpanded,
                onDismissRequest = { isApiMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.api_type_kobold_horde)) },
                    onClick = {
                        onEvent(COUserEvent.UpdateSelectedApi(API_TYPE.HORDE))
                        isApiMenuExpanded = false
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.api_type_kobold)) },
                    onClick = {
                        onEvent(COUserEvent.UpdateSelectedApi(API_TYPE.KOBOLD_AI))
                        isApiMenuExpanded = false
                    })
            }
        }

        AnimatedContent(
            targetState = state.selectedApiType, label = "",
            modifier = Modifier.fillMaxSize()
        ) { apiType ->
            when (apiType) {
                API_TYPE.KOBOLD_AI -> KoboldAiFragment(modifier = Modifier, onEvent = onEvent)
                API_TYPE.HORDE -> HordeFragment(
                    state = state.hordeFragmentState,
                    modifier = Modifier, onEvent = onEvent
                )
            }
        }
    }
}

fun apiTypeStringResource(apiType: API_TYPE): Int {
    return when (apiType) {
        API_TYPE.KOBOLD_AI -> R.string.api_type_kobold
        API_TYPE.HORDE -> R.string.api_type_kobold_horde
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
                snackbarHostState = SnackbarHostState(),
                onEvent = {}
            )
        }
    }
}