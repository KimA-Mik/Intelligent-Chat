package ru.kima.intelligentchat.presentation.personas.list

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToPersona
import ru.kima.intelligentchat.presentation.personas.common.PersonaImage
import ru.kima.intelligentchat.presentation.personas.list.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent
import ru.kima.intelligentchat.presentation.personas.list.model.PersonaItem
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
            thumbnails = state.thumbnails,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
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

//TODO: Fix recomposing whole list on select
@Composable
fun PersonaListContent(
    personas: ImmutableList<PersonaItem>,
    thumbnails: ImmutableList<Bitmap?>,
    modifier: Modifier,
    onEvent: (UserEvent) -> Unit
) {
    LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(personas.size, key = { personas[it].id }) { i ->
            val persona = personas[i]
            PersonaCard(
                persona = persona,
                thumbnail = thumbnails.getOrNull(i),
                modifier = Modifier.fillMaxWidth(),
                onCardClicked = { onEvent(UserEvent.NavigateToPersona(persona.id)) },
                onAvatarClicked = {},
                onSelectClicked = { onEvent(UserEvent.SelectPersona(persona.id)) }
            )
        }
    }
}

@Composable
fun PersonaCard(
    persona: PersonaItem,
    thumbnail: Bitmap?,
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
    onAvatarClicked: () -> Unit,
    onSelectClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onCardClicked),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PersonaImage(
            bitmap = thumbnail, modifier = Modifier
                .size(88.dp)
                .padding(8.dp),
            onClick = onAvatarClicked
        )
        Text(
            text = persona.name, style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .fillMaxHeight()
        )
        IconButton(
            onClick = onSelectClicked,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = if (persona.selected) Icons.Outlined.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = stringResource(R.string.select_persona_button_content_description)
            )
        }
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
                state = PersonasListState(
                    personas = List(5) {
                        PersonaItem(
                            id = it.toLong(),
                            name = "Persona $it",
                            selected = it == 2
                        )
                    }.toImmutableList(),
                ),
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState(),
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                events = MutableStateFlow(Event(null)),
                onEvent = {}
            )
        }
    }
}