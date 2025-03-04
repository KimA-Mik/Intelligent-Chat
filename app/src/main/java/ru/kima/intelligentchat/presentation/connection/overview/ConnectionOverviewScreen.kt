package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeEvent
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.presentation.connection.overview.events.COUiEvent
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.overview.fragments.HordeFragment
import ru.kima.intelligentchat.presentation.connection.overview.fragments.KoboldAiFragment
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToHordePreset
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConnectionOverviewScreen(
    expanded: Boolean,
    state: ConnectionOverviewState,
    uiEvents: ComposeEvent<COUiEvent>,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    onEvent: (COUserEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    ConsumeEvent(uiEvents, snackbarHostState, scope, navController)

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    //Open drawer button kept recomposing, looked ugly
    val openDrawer = remember<() -> Unit> {
        {
            scope.launch {
                drawerState.open()
            }
        }
    }
    //and scroll behavior is unstable too
    //whats the point of composable behavior if it make top bar recompose every time...
    val sb = remember { scrollBehavior }

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
                    if (!expanded) {
                        IconButton(onClick = openDrawer) {
                            Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu")
                        }
                    }
                },
                scrollBehavior = sb,
            )
        },
        modifier = Modifier
            .imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ConnectionOverviewContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .nestedScroll(sb.nestedScrollConnection)
        )
    }
}

@Composable
fun ConsumeEvent(
    event: ComposeEvent<COUiEvent>,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    navController: NavController
) {
    val value = event.value
    value?.let {
        when (it) {
            is COUiEvent.ShowMessage -> scope.launch { snackbarHostState.showSnackbar(it.message) }
            is COUiEvent.ShowSnackbar -> ShowSnackbar(
                snackbar = it.snackbar,
                snackbarHostState = snackbarHostState,
            )

            is COUiEvent.EditPreset -> navController.navigateToHordePreset(it.presetId)
        }
    }
}

@Composable
fun ShowSnackbar(
    snackbar: COUiEvent.COSnackbar,
    snackbarHostState: SnackbarHostState,
) {
    val message = when (snackbar) {
        COUiEvent.COSnackbar.ErrorGetKudos -> stringResource(R.string.error_get_kudos_snackbar)
        COUiEvent.COSnackbar.NoUser -> stringResource(R.string.no_horde_user_snackbar)
        is COUiEvent.COSnackbar.ShowKudos -> stringResource(
            R.string.kudos_template_snackbar,
            snackbar.kudos.toInt()
        )

        COUiEvent.COSnackbar.ApiKeySaved -> stringResource(R.string.horde_api_key_is_saved_snackbar)
        COUiEvent.COSnackbar.HordeUserNotFound -> stringResource(R.string.horde_user_not_found_snackbar)
        COUiEvent.COSnackbar.HordeValidationError -> stringResource(R.string.horde_validation_error_snackbar)
        COUiEvent.COSnackbar.NoInternet -> stringResource(R.string.no_internet_connection_snackbar)
        COUiEvent.COSnackbar.EmptyHordeKey -> stringResource(R.string.horde_key_empty_snackbar)
        is COUiEvent.COSnackbar.HordeUnknownError -> stringResource(
            R.string.unknown_horde_error_snackbar,
            snackbar.message
        )

        is COUiEvent.COSnackbar.ModelsUpdated -> stringResource(
            R.string.load_models_snackbar,
            snackbar.modelsCount
        )
    }

    LaunchedEffect(snackbar) {
        snackbarHostState.showSnackbar(message)
    }
}

@Composable
fun ConnectionOverviewContent(
    state: ConnectionOverviewState,
    modifier: Modifier,
    onEvent: (COUserEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Box {
            var isApiMenuExpanded by remember { mutableStateOf(false) }
            val rotation by animateFloatAsState(
                targetValue = if (isApiMenuExpanded) 0f else 180f,
                label = "rotation"
            )
            TextField(
                value = apiTypeStringResource(state.selectedApiType),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { isApiMenuExpanded = true }) {
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
            )
            DropdownMenu(
                expanded = isApiMenuExpanded,
                onDismissRequest = { isApiMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.api_type_kobold_horde)) },
                    onClick = {
                        onEvent(COUserEvent.UpdateSelectedApi(ApiType.HORDE))
                        isApiMenuExpanded = false
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.api_type_kobold)) },
                    onClick = {
                        onEvent(COUserEvent.UpdateSelectedApi(ApiType.KOBOLD_AI))
                        isApiMenuExpanded = false
                    })
            }
        }

        AnimatedContent(
            targetState = state.selectedApiType, label = "",
            modifier = Modifier.fillMaxSize()
        ) { apiType ->
            when (apiType) {
                ApiType.KOBOLD_AI -> KoboldAiFragment(modifier = Modifier, onEvent = onEvent)
                ApiType.HORDE -> HordeFragment(
                    state = state.hordeFragmentState,
                    modifier = Modifier, onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun apiTypeStringResource(apiType: ApiType): String {
    return when (apiType) {
        ApiType.KOBOLD_AI -> stringResource(id = R.string.api_type_kobold)
        ApiType.HORDE -> stringResource(id = R.string.api_type_kobold_horde)
    }
}

@Preview
@Composable
fun ConnectionOverviewPreview() {
    IntelligentChatTheme {
        Surface(Modifier.fillMaxSize()) {
            ConnectionOverviewScreen(
                expanded = false,
                state = ConnectionOverviewState(),
                uiEvents = ComposeEvent(COUiEvent.ShowMessage("123")),
                drawerState = DrawerState(initialValue = DrawerValue.Closed),
                snackbarHostState = SnackbarHostState(),
                navController = rememberNavController(),
                onEvent = {}
            )
        }
    }
}