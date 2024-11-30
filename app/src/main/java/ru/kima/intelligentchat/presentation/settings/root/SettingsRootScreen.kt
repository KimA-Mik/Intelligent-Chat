package ru.kima.intelligentchat.presentation.settings.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.settings.PreferenceScaffold
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootAction
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootUiEvent
import ru.kima.intelligentchat.presentation.settings.screen.AdvancedFormattingScreen
import ru.kima.intelligentchat.presentation.settings.screen.ChatSettingsScreen
import ru.kima.intelligentchat.presentation.settings.screen.SettingsScreen
import ru.kima.intelligentchat.presentation.settings.screen.components.SettingsNavItem
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SettingsRoot(
    expanded: Boolean,
    drawerState: DrawerState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: SettingsRootViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
    val onEvent = remember<(SettingsRootAction) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    SettingsRootScreen(
        state = state,
        uiEvent = uiEvent,
        expanded = expanded,
        drawerState = drawerState,
        navController = navController,
        snackbarHostState = snackbarHostState,
        onEvent = onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    state: SettingsRootState,
    uiEvent: Event<SettingsRootUiEvent>,
    expanded: Boolean,
    drawerState: DrawerState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onEvent: (SettingsRootAction) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(uiEvent) {
        uiEvent.consume {
            when (it) {
                SettingsRootUiEvent.NotImplemented -> scope.launch {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.not_implemented_placeholder),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    if (state.selectedScreen == null) {
        ListScaffold(
            screens = state.screens,
            scope = scope,
            expanded, drawerState, snackbarHostState, onEvent
        )
    } else {
        PreferenceScaffold(
            titleRes = state.selectedScreen.titleRes(),
            itemsProvider = { state.selectedScreen.settings() },
            onBackPressed = { onEvent(SettingsRootAction.ClearScreen) }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ListScaffold(
    screens: ImmutableList<SettingsScreen>,
    scope: CoroutineScope,
    expanded: Boolean,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SettingsRootAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            AppBar(
                titleContent = {
                    Text(text = stringResource(R.string.nav_item_settings))
                },
                navigateUp = if (!expanded) {
                    {
                        scope.launch { drawerState.open() }
                    }
                } else null,
                navigationIcon = Icons.Default.Menu,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        SettingsRootContent(
            screens = screens,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun SettingsRootContent(
    screens: ImmutableList<SettingsScreen>,
    onEvent: (SettingsRootAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        val navItemModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        items(screens) { screen ->
            SettingsNavItem(
                title = stringResource(screen.titleRes()),
                onNavigate = { onEvent(SettingsRootAction.SelectScreen(screen)) },
                modifier = navItemModifier,
                description = screen.subtitleRes()?.let {
                    stringResource(it)
                },
                icon = screen.icon()
            )
        }
    }
}

@Preview
@Composable
private fun SettingsRootScreenPreview() {
    IntelligentChatTheme {
        Surface {
            SettingsRootScreen(
                state = SettingsRootState(
                    persistentListOf(
                        ChatSettingsScreen,
                        AdvancedFormattingScreen
                    )
                ),
                uiEvent = Event(null),
                expanded = false,
                drawerState = rememberDrawerState(DrawerValue.Closed),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                onEvent = {}
            )
        }
    }
}