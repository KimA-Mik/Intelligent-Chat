package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.presentation.common.image.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    viewModel: CardDetailsViewModel
) {
    val onEvent = remember<(CardDetailUserEvent) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }
    BackHandler(onBack = {
        navController.popBackStack()
    })
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        onEvent(CardDetailUserEvent.UpdateCardImage(imageBytes))
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                UiEvent.SelectImage -> {
                    imagePicker.pickImage()
                }

                is UiEvent.SnackbarMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            uiEvent.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                UiEvent.PopBack -> navController.popBackStack()
            }
        }
    }

    when {
        state.deleteCardDialog -> SimpleAlertDialog(
            onConfirm = { onEvent(CardDetailUserEvent.ConfirmDeleteCard) },
            onDismiss = { onEvent(CardDetailUserEvent.DismissDeleteCard) },
            title = stringResource(R.string.delete_card_dialog_title),
            text = stringResource(R.string.delete_card_dialog_text),
            icon = Icons.Filled.DeleteForever,
            confirmText = stringResource(R.string.delete_button_text),
            dismissText = stringResource(R.string.cancel_button_text)
        )

        state.deleteAltGreetingDialog -> SimpleAlertDialog(
            onConfirm = { onEvent(CardDetailUserEvent.ConfirmDeleteAltGreeting) },
            onDismiss = { onEvent(CardDetailUserEvent.DismissDeleteAltGreeting) },
            title = stringResource(R.string.delete_alternate_greeting_dialog_title),
            text = stringResource(R.string.delete_alternate_greeting_dialog_text),
            icon = Icons.Filled.DeleteForever,
            confirmText = stringResource(R.string.delete_button_text),
            dismissText = stringResource(R.string.cancel_button_text)
        )
    }

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .imePadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.card.name,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    CardDropdownMenu(onEvent)
                },
            )
        },
    ) { contentPadding ->
        if (state.showAltGreeting) {
            ModalBottomSheet(onDismissRequest = {
                onEvent(CardDetailUserEvent.CloseAltGreetingsSheet)
            }) {
                AltGreetingsSheetContent(
                    greetings = state.card.alternateGreetings,
                    editableGreeting = state.editableGreeting,
                    editableGreetingBuffer = state.editableGreetingBuffer,
                    onEvent = onEvent
                )
            }
        }

        CardDetailContent(
            state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun CardDropdownMenu(
    onEvent: (CardDetailUserEvent) -> Unit
) {
    Box {
        var dropdownMenu by remember { mutableStateOf(false) }
        IconButton(onClick = { dropdownMenu = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Save card",
            )
        }

        DropdownMenu(expanded = dropdownMenu, onDismissRequest = { dropdownMenu = false }) {
            //TODO: Move string constants away when JB fix Android Studio
            DropdownMenuItem(
                text = { Text(text = "Delete card") },
                onClick = { onEvent(CardDetailUserEvent.DeleteCardClicked) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete card"
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Save card") },
                onClick = { onEvent(CardDetailUserEvent.SaveCard) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save card",
                    )
                }
            )
        }
    }
}