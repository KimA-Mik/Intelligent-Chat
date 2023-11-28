package ru.kima.intelligentchat.presentation.charactersList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUiEvent

@Composable
fun CharactersListScreen(
    navController: NavController,
    viewModel: CharactersListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is CharactersListUiEvent.NavigateTo -> {
                    navController.navigate("cards/${event.cardId}")
                }
            }
        }
    }

    CharactersListContent(state) { event ->
        viewModel.onUserEvent(event)
    }
}
