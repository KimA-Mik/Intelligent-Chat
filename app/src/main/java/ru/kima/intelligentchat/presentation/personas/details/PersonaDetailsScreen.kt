package ru.kima.intelligentchat.presentation.personas.details

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.personas.common.PersonaImage
import ru.kima.intelligentchat.presentation.personas.details.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.details.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    state: PersonaDetailsState,
    uiEvents: StateFlow<Event<UiEvent>>,
    onEvent: (UserEvent) -> Unit
) {
    imagePicker.registerPicker { imageBytes ->
        onEvent(UserEvent.UpdatePersonaImage(imageBytes))
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        uiEvents.collect { event ->
            consumeEvent(event, context, scope, snackbarHostState, navController)
        }
    }

    var deletePersonaDialog by remember { mutableStateOf(false) }

    when {
        deletePersonaDialog -> SimpleAlertDialog(
            onConfirm = {
                deletePersonaDialog = false
                onEvent(UserEvent.DeletePersona)
            },
            onDismiss = { deletePersonaDialog = false },
            title = stringResource(R.string.delete_persona_dialog_title),
            text = stringResource(R.string.delete_persona_dialog_text),
            icon = Icons.Filled.DeleteForever
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Persona",
                    style = MaterialTheme.typography.headlineSmall
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }, actions = {
                IconButton(onClick = { deletePersonaDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_persona_content_description)
                    )
                }
                IconButton(onClick = { onEvent(UserEvent.SavePersona) }) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = stringResource(R.string.save_persona_content_description)
                    )
                }
            })
        }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        PersonaDetailsContent(
            modifier = Modifier
                .padding(paddingValues),
            state = state,
            imagePicker = imagePicker,
            onEvent = onEvent
        )
    }
}

fun consumeEvent(
    event: Event<UiEvent>,
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    event.consume { current ->
        when (current) {
            is UiEvent.ShowSnackbar -> {
                val message = context.getString(current.message.mesId)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message, duration = SnackbarDuration.Short
                    )
                }
            }

            UiEvent.PopBack -> navController.popBackStack()
        }
    }
}

@Composable
fun PersonaDetailsContent(
    modifier: Modifier = Modifier,
    state: PersonaDetailsState,
    imagePicker: ImagePicker,
    onEvent: (UserEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PersonaImage(
            container = state.personaImage,
            modifier = Modifier.size(144.dp),
            onClick = {
                imagePicker.pickImage()
            }
        )

        OutlinedTextField(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            label = { Text(stringResource(id = R.string.persona_name)) },
            value = state.personaName,
            onValueChange = { value ->
                onEvent(
                    UserEvent.UpdatePersonaDetailsField(
                        PersonaDetailsViewModel.PersonaDetailsField.NAME, value
                    )
                )
            }, supportingText = {
                TokensCountText(tokens = state.tokens.nameTokens)
            })

        OutlinedTextField(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            label = { Text(stringResource(id = R.string.persona_description)) },
            value = state.personaDescription,
            onValueChange = { value ->
                onEvent(
                    UserEvent.UpdatePersonaDetailsField(
                        PersonaDetailsViewModel.PersonaDetailsField.DESCRIPTION, value
                    )
                )
            }, supportingText = {
                TokensCountText(tokens = state.tokens.descriptionTokens)
            })
    }
}

@Composable
fun TokensCountText(tokens: Int) {
    Text(
        text = stringResource(id = R.string.tokens_count, tokens),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
    )
}


@Preview
@Composable
fun PersonaDetailsScreenPreview() {
    IntelligentChatTheme {
        PersonaDetailsScreen(
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            imagePicker = ImagePicker(LocalContext.current),
            state = PersonaDetailsState(),
            uiEvents = MutableStateFlow(Event(null)),
            onEvent = {})
    }
}