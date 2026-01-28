package ru.kima.intelligentchat.presentation.characterCard.charactersList

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.common.components.SearchToolbar
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.common.util.runSnackbar
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToCardChat
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToCardEdit
import ru.kima.intelligentchat.presentation.navigation.navigateToCardImage
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersListScreenRoot(
    expanded: Boolean,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState
) {
    val viewModel: CharactersListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvents.collectAsStateWithLifecycle()
    val onEvent = remember<(CharactersListUserEvent) -> Unit> {
        {
            viewModel.onUserEvent(it)
        }
    }
    CharactersListScreen(
        state = state,
        onEvent = onEvent,
        uiEvent = uiEvent,
        navController = navController,
        snackbarHostState = snackbarHostState,
        drawerState = drawerState,
        imagePicker = koinInject(),
        expanded = expanded
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CharactersListScreen(
    state: CharactersListState,
    onEvent: (CharactersListUserEvent) -> Unit,
    uiEvent: Event<CharactersListUiEvent>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    imagePicker: ImagePicker,
    expanded: Boolean
) {
    val context = LocalContext.current
    imagePicker.registerPicker { imageBytes ->
        onEvent(CharactersListUserEvent.AddCardFromImage(imageBytes))
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(uiEvent) {
        uiEvent.consume { event ->
            when (event) {
                is CharactersListUiEvent.NavigateToCardEdit ->
                    navController.navigateToCardEdit(event.cardId)

                is CharactersListUiEvent.NavigateToCardChat ->
                    navController.navigateToCardChat(event.cardId)

                is CharactersListUiEvent.SnackbarMessage -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }

                CharactersListUiEvent.SelectPngImage ->
                    imagePicker.pickImage("image/png")

                is CharactersListUiEvent.ShowCardImage ->
                    navController.navigateToCardImage(event.cardId)

                is CharactersListUiEvent.Message -> scope.launch {
                    snackbarHostState.showSnackbar(context.getString(event.messageId))
                }

                CharactersListUiEvent.OpenNavigationDrawer -> scope.launch {
                    drawerState.open()
                }

                is CharactersListUiEvent.CardDeleted -> scope.launch {
                    runSnackbar(
                        snackbarHostState = snackbarHostState,
                        message = context.getString(R.string.message_deleted_snackbar_message),
                        onActionPerformed = {
                            onEvent(CharactersListUserEvent.RestoreCardClicked(event.cardId))
                        },
                        actionLabel = context.getString(R.string.restore_snackbar_action),
                    )
                }
            }
        }
    }

    when {
        state.initialDialog -> InitPersonaDialog(
            text = state.initialDialogText,
            onDismissRequest = { onEvent(CharactersListUserEvent.DismissInitialPersonaName) },
            onAcceptDialog = { onEvent(CharactersListUserEvent.AcceptInitialPersonaName) },
            onTextChanged = { text ->
                onEvent(
                    CharactersListUserEvent.InitDialogValueChanged(
                        text
                    )
                )
            }
        )
    }

    var isExpanded by retain { mutableStateOf(false) }
    BackHandler(isExpanded) { isExpanded = false }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            ChatListAppBar(
                searchQuery = state.searchText,
                onChangeSearchQuery = { onEvent(CharactersListUserEvent.SearchQueryChanged(it)) },
                showNavigationIcon = !expanded,
                onNavigationIconClick = { onEvent(CharactersListUserEvent.OnMenuButtonClicked) },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FabMenu(
                isExpanded,
                fabVisible = true,
                onImageButtonClick = {
                    onEvent(CharactersListUserEvent.AddCardFromImageClicked)
                },
                onCreateButtonClick = {
                    onEvent(CharactersListUserEvent.CreateCardClicked)
                },
                onControlButtonClick = {
                    isExpanded = !isExpanded
                }
            )

//            ActionButtons(
//                isExpanded,
//                onImageButtonClick = {
//                    onEvent(CharactersListUserEvent.AddCardFromImageClicked)
//                },
//                onCreateButtonClick = {
//                    onEvent(CharactersListUserEvent.CreateCardClicked)
//                },
//                onControlButtonClick = {
//                    isExpanded = !isExpanded
//                })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        CharactersList(
            cards = state.cards,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListAppBar(
    searchQuery: String?,
    onChangeSearchQuery: (String?) -> Unit,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    SearchToolbar(
        searchQuery = searchQuery,
        onChangeSearchQuery = onChangeSearchQuery,
        titleContent = { Text(text = stringResource(R.string.nav_item_characters)) },
        navigateUp = if (showNavigationIcon) onNavigationIconClick else null,
        navigationIcon = Icons.Default.Menu,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun ActionButtons(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onImageButtonClick: () -> Unit,
    onCreateButtonClick: () -> Unit,
    onControlButtonClick: () -> Unit
) {
    Column(modifier = modifier) {
        AnimatedVisibility(isExpanded) {
            val padding = Modifier.padding(bottom = 16.dp)
            Column {
                SmallFab(
                    onClick = onImageButtonClick,
                    modifier = padding
                ) {
                    Icon(Icons.Filled.Image, contentDescription = "From image")
                }

                SmallFab(
                    onClick = onCreateButtonClick,
                    modifier = padding
                ) {
                    Icon(Icons.Filled.Create, contentDescription = "Create Card")
                }
            }
        }

        FloatingActionButton(
            onClick = onControlButtonClick,
            shape = MaterialTheme.shapes.medium
        ) {
            AnimatedContent(
                isExpanded,
                transitionSpec = {
                    expandIn(expandFrom = Alignment.TopStart) togetherWith
                            fadeOut(animationSpec = tween(100))
                }, label = "Fab icon"
            ) { targetState ->
                Icon(
                    if (targetState) Icons.Filled.Close else Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun SmallFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface,
        elevation = FloatingActionButtonDefaults.elevation(2.dp),
        contentColor = MaterialTheme.colorScheme.onSurface,
        content = content
    )
}

@Composable
fun InitPersonaDialog(
    text: String,
    onDismissRequest: () -> Unit,
    onAcceptDialog: () -> Unit,
    onTextChanged: (String) -> Unit,
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.introduce_first_persona),
                    textAlign = TextAlign.Center,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = onTextChanged,
                    label = { Text(text = "{{user}}") },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(R.string.decide_later_button_text))
                    }

                    TextButton(onClick = onAcceptDialog) {
                        Text(text = stringResource(R.string.confirm_button_text))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FabMenu(
    isExpanded: Boolean,
    fabVisible: Boolean,
    modifier: Modifier = Modifier,
    onImageButtonClick: () -> Unit,
    onCreateButtonClick: () -> Unit,
    onControlButtonClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    FloatingActionButtonMenu(
        expanded = isExpanded,
        button = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(
                        if (isExpanded) {
                            TooltipAnchorPosition.Start
                        } else {
                            TooltipAnchorPosition.Above
                        }
                    ),
                tooltip = { PlainTooltip { Text(stringResource(R.string.tooltip_add_character)) } },
                state = rememberTooltipState(),
            ) {
                ToggleFloatingActionButton(
                    modifier =
                        Modifier
                            .animateFloatingActionButton(
                                visible = fabVisible || isExpanded,
                                alignment = Alignment.BottomEnd,
                            )
                            .focusRequester(focusRequester),
                    checked = isExpanded,
                    onCheckedChange = { onControlButtonClick() },
                ) {
                    val imageVector by remember {
                        derivedStateOf {
                            if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                        }
                    }
                    Icon(
                        painter = rememberVectorPainter(imageVector),
                        contentDescription = null,
                        modifier = Modifier.animateIcon({ checkedProgress }),
                    )
                }
            }
        },
        modifier = modifier
    ) {
        var string = stringResource(R.string.fab_button_import_from_png)
        FloatingActionButtonMenuItem(
            onClick = onImageButtonClick,
            text = { Text(string) },
            icon = { Icon(Icons.Filled.Image, contentDescription = string) }
        )

        string = stringResource(R.string.fab_button_create_card)
        FloatingActionButtonMenuItem(
            onClick = onCreateButtonClick,
            text = { Text(string) },
            icon = { Icon(Icons.Filled.Create, contentDescription = string) }
        )
    }
}

@Preview(name = "Characters Screen Preview light theme")
@Preview(
    name = "Characters Screen Preview dark theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CharactersScreenPreview() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val cards = List(100) { index ->
                ImmutableCardEntry(
                    id = index.toLong(),
                    name = "Name $index",
                    characterVersion = "Version $index",
                    creatorNotes = "Notes $index"
                )
            }.toImmutableList()
            CharactersListScreen(
                state = CharactersListState(cards),
                onEvent = { },
                uiEvent = Event(null),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                drawerState = rememberDrawerState(DrawerValue.Closed),
                imagePicker = ImagePicker(LocalContext.current),
                expanded = false
            )

        }
    }
}

@Preview(
    name = "Characters Screen Preview expanded",
    widthDp = 600
)
@Composable
fun CharactersScreenPreviewExpanded() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val cards = List(100) { index ->
                ImmutableCardEntry(
                    id = index.toLong(),
                    name = "Name $index",
                    characterVersion = "Version $index",
                    creatorNotes = "Notes $index"
                )
            }.toImmutableList()
            CharactersListScreen(
                state = CharactersListState(cards),
                onEvent = { },
                uiEvent = Event(null),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                drawerState = rememberDrawerState(DrawerValue.Closed),
                imagePicker = ImagePicker(LocalContext.current),
                expanded = true
            )
        }
    }
}