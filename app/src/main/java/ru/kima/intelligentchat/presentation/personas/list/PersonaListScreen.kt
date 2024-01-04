package ru.kima.intelligentchat.presentation.personas.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToPersona
import ru.kima.intelligentchat.presentation.personas.common.PersonaImage
import ru.kima.intelligentchat.presentation.personas.list.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaListScreen(
    state: PersonasListState,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    events: StateFlow<Event<UiEvent>>,
    onEvent: (UserEvent) -> Unit
) {
    LaunchedEffect(true) {
        events.collect { event ->
            consumeEvent(event, navController)
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Persona") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.Menu, contentDescription = "Menu")
                    }
                })
        },
        floatingActionButton = {
            CreatePersonaFab {
                onEvent(UserEvent.CreatePersona)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        PersonaListContent(
            personas = state.personas,
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent
        )
    }
}

fun consumeEvent(
    event: Event<UiEvent>,
    navController: NavController
) {
    event.consume { current ->
        current?.let {
            when (it) {
                is UiEvent.NavigateToPersona -> navController.navigateToPersona(it.id)
            }
        }
    }
}

@Composable
fun PersonaListContent(
    personas: List<Persona>,
    modifier: Modifier,
    onEvent: (UserEvent) -> Unit
) {
    LazyColumn(modifier) {
        items(personas) { persona ->
            PersonaCard(
                persona = persona,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onCardClicked = { onEvent(UserEvent.NavigateToPersona(persona.id)) },
                onAvatarClicked = {}
            )
        }
    }
}

@Composable
fun PersonaCard(
    persona: Persona,
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
    onAvatarClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onCardClicked),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //TODO: Fx image
        PersonaImage(
            bitmap = null, modifier = Modifier.size(72.dp),
            onClick = onAvatarClicked
        )
        Text(text = persona.name, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun CreatePersonaFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Create,
            contentDescription = stringResource(R.string.create_persona_fab_content_description)
        )
    }
}


@Preview
@Composable
fun PersonasListScreenPreview() {
    IntelligentChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PersonaListScreen(
                state = PersonasListState(personas = List(5) {
                    Persona(
                        id = it.toLong(),
                        name = "Persona $it"
                    )
                }),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                events = MutableStateFlow(Event(null)),
                onEvent = {}
            )
        }
    }
}