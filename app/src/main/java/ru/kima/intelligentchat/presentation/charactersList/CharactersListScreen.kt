package ru.kima.intelligentchat.presentation.charactersList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.common.image.ImagePicker

@Composable
fun CharactersListScreen(
    navController: NavController,
    imagePicker: ImagePicker = koinInject(),
    viewModel: CharactersListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        viewModel.onUserEvent(CharactersListUserEvent.AddCardFromImage(imageBytes))
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is CharactersListUiEvent.NavigateTo -> {
                    navController.navigate("cards/${event.cardId}")
                }

                is CharactersListUiEvent.SnackbarMessage ->
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message,
                            duration = SnackbarDuration.Short
                        )
                    }

                CharactersListUiEvent.SelectPngImage -> {
                    imagePicker.pickImage("image/png")
                }
            }
        }
    }



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    viewModel.onUserEvent(CharactersListUserEvent.AddCardFromImageClicked)
                }) {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CharactersListContent(state) { event ->
                viewModel.onUserEvent(event)
            }
        }
    }

}