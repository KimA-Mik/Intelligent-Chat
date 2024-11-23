package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardDetailsRoot(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    viewModel: CardDetailsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvents.collectAsStateWithLifecycle()
    val onEvent = remember<(CardDetailUserEvent) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    CardDetailsScreen(
        state = state,
        uiEvent = uiEvent,
        navController = navController,
        snackbarHostState = snackbarHostState,
        imagePicker = imagePicker,
        onEvent = onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    state: CardDetailsState,
    uiEvent: Event<UiEvent>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    onEvent: (CardDetailUserEvent) -> Unit
) {
    BackHandler(onBack = {
        navController.popBackStack()
    })
    imagePicker.registerPicker { imageBytes ->
        onEvent(CardDetailUserEvent.UpdateCardImage(imageBytes))
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(uiEvent) {
        uiEvent.consume { uiEvent ->
            when (uiEvent) {
                UiEvent.SelectImage -> imagePicker.pickImage()

                is UiEvent.SnackbarMessage -> scope.launch {
                    snackbarHostState.showSnackbar(
                        uiEvent.message,
                        duration = SnackbarDuration.Short
                    )
                }

                UiEvent.PopBack -> navController.popBackStack()
            }
        }
    }

    when {
        state.additionalSurfaces.deleteAltGreetingDialog -> SimpleAlertDialog(
            onConfirm = { onEvent(CardDetailUserEvent.ConfirmDeleteAltGreeting) },
            onDismiss = { onEvent(CardDetailUserEvent.DismissDeleteAltGreeting) },
            title = stringResource(R.string.delete_alternate_greeting_dialog_title),
            text = stringResource(R.string.delete_alternate_greeting_dialog_text),
            icon = Icons.Filled.DeleteForever,
            confirmText = stringResource(R.string.delete_button_text),
            dismissText = stringResource(R.string.cancel_button_text)
        )

        state.additionalSurfaces.showAltGreeting -> ModalBottomSheet(
            onDismissRequest = { onEvent(CardDetailUserEvent.CloseAltGreetingsSheet) },
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        ) {
            AltGreetingsSheetContent(
                greetings = state.card.alternateGreetings,
                editableGreeting = state.additionalSurfaces.editableGreeting,
                editableGreetingBuffer = state.additionalSurfaces.editableGreetingBuffer,
                onEvent = onEvent
            )
        }

    }

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior()
    val sb = remember { scrollBehavior }
    Scaffold(
        modifier = Modifier
            .imePadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CardDetailsAppBar(
                name = state.card.name,
                cardTokens = state.tokensCount.totalTokens,
                nameTokensCount = state.tokensCount.name,
                photoName = state.card.photoName,
                sb, onEvent
            )
        },
    ) { contentPadding ->
        CardDetailContent(
            state,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .nestedScroll(sb.nestedScrollConnection)
        )
    }
}

const val OVERLAPPED_FRACTION = 0.1f

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CardDetailsAppBar(
    name: String,
    cardTokens: Int,
    nameTokensCount: Int,
    photoName: String?,
    scrollBehavior: TopAppBarScrollBehavior,
    onEvent: (CardDetailUserEvent) -> Unit
) = Column {
    val headVisible = scrollBehavior.state.overlappedFraction < OVERLAPPED_FRACTION
    AppBar(
        titleContent = {
            AnimatedVisibility(
                visible = !headVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = name,
                    maxLines = 2
                )
            }
        },
        navigateUp = { onEvent(CardDetailUserEvent.NavigateUp) },
        colors = TopAppBarDefaults.largeTopAppBarColors(scrolledContainerColor = TopAppBarDefaults.largeTopAppBarColors().containerColor),
        scrollBehavior = scrollBehavior
    )

    AnimatedVisibility(
        visible = headVisible,
    ) {
        HeadArea(
            name = name,
            cardTokens = cardTokens,
            nameTokensCount = nameTokensCount,
            photoName = photoName,
            modifier = Modifier.padding(horizontal = 8.dp),
            onEvent = onEvent,
        )
    }
}

@Preview(name = "Card details screen light mode")
@Preview(
    name = "Card details screen dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewCardDetailsScreen() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val card = ImmutableCard(
                name = "Name",
                description = "Description"
            )
            CardDetailsScreen(
                state = CardDetailsState(card),
                uiEvent = Event(null),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                imagePicker = ImagePicker(LocalContext.current),
                onEvent = {}
            )
        }
    }
}