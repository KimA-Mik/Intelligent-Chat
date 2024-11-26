package ru.kima.intelligentchat.presentation.settings.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TextFormat
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.settings.components.SettingsNavItem
import ru.kima.intelligentchat.presentation.settings.events.SettingsRootAction
import ru.kima.intelligentchat.presentation.settings.events.SettingsRootUiEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SettingsRoot(
    expanded: Boolean,
    drawerState: DrawerState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: SettingsRootViewModel = koinViewModel()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
    val onEvent = remember<(SettingsRootAction) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    SettingsRootScreen(
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
    uiEvent: Event<SettingsRootUiEvent>,
    expanded: Boolean,
    drawerState: DrawerState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onEvent: (SettingsRootAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
    onEvent: (SettingsRootAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        val navItemModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        SettingsNavItem(
            title = stringResource(R.string.settings_nav_item_chat_appearance_title),
            onNavigate = { onEvent(SettingsRootAction.OpenChatAppearance) },
            modifier = navItemModifier,
            description = stringResource(R.string.settings_nav_item_chat_appearance_description),
            icon = Icons.AutoMirrored.Filled.Chat
        )

        SettingsNavItem(
            title = stringResource(R.string.settings_nav_item_advanced_formatting_title),
            onNavigate = { onEvent(SettingsRootAction.OpenAdvancedFormatting) },
            modifier = navItemModifier,
            description = stringResource(R.string.settings_nav_item_advanced_formatting_description),
            icon = Icons.Default.TextFormat
        )
    }
}

@Preview
@Composable
private fun SettingsRootScreenPreview() {
    IntelligentChatTheme {
        Surface {
            SettingsRootScreen(
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