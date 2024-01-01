package ru.kima.intelligentchat.presentation.personas.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        uiEvents.collect { event ->
            event.consume { current ->
                current?.let {
                    when (it) {
                        is UiEvent.ShowSnackbar -> {
                            val message = context.getString(it.message.mesId)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message, duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Persona") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }, actions = {
            IconButton(onClick = { onEvent(UserEvent.SavePersona) }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Save"
                )
            }
        })
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        PersonaDetailsContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
fun PersonaDetailsContent(
    modifier: Modifier = Modifier,
    state: PersonaDetailsState,
    onEvent: (UserEvent) -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(modifier = Modifier.padding(8.dp),
            value = state.personaName,
            onValueChange = { value ->
                onEvent(
                    UserEvent.UpdatePersonaDetailsField(
                        PersonaDetailsViewModel.PersonaDetailsField.NAME, value
                    )
                )
            })

        OutlinedTextField(modifier = Modifier.padding(8.dp),
            value = state.personaDescription,
            onValueChange = { value ->
                onEvent(
                    UserEvent.UpdatePersonaDetailsField(
                        PersonaDetailsViewModel.PersonaDetailsField.DESCRIPTION, value
                    )
                )
            })
    }
}


@Preview
@Composable
fun PersonaDetailsScreenPreview() {
    IntelligentChatTheme {
        PersonaDetailsScreen(navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            imagePicker = ImagePicker(LocalContext.current),
            state = PersonaDetailsState(),
            uiEvents = MutableStateFlow(Event(null)),
            onEvent = {})
    }
}