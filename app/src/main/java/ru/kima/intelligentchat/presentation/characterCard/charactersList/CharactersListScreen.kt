package ru.kima.intelligentchat.presentation.characterCard.charactersList

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
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToCard
import ru.kima.intelligentchat.presentation.navigation.navigateToCardImage

@Composable
fun CharactersListScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    imagePicker: ImagePicker,
    viewModel: CharactersListViewModel
) {
    val onEvent = remember<(CharactersListUserEvent) -> Unit> {
        {
            viewModel.onUserEvent(it)
        }
    }

    val content = LocalContext.current
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        onEvent(CharactersListUserEvent.AddCardFromImage(imageBytes))
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is CharactersListUiEvent.NavigateToCard ->
                    navController.navigateToCard(event.cardId)

                is CharactersListUiEvent.SnackbarMessage ->
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }

                CharactersListUiEvent.SelectPngImage ->
                    imagePicker.pickImage("image/png")

                is CharactersListUiEvent.ShowCardImage ->
                    navController.navigateToCardImage(event.cardId)

                is CharactersListUiEvent.Message ->
                    snackbarHostState.showSnackbar(content.getString(event.messageId))

                CharactersListUiEvent.OpenNavigationDrawer -> scope.launch {
                    drawerState.open()
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


    var isExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            ActionButtons(
                isExpanded,
                onImageButtonClick = {
                    onEvent(CharactersListUserEvent.AddCardFromImageClicked)
                },
                onCreateButtonClick = {
                    onEvent(CharactersListUserEvent.CreateCardClicked)
                },
                onControlButtonClick = {
                    isExpanded = !isExpanded
                })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        CharactersListContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state, onEvent = onEvent
        )
    }
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