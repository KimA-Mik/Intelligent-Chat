package ru.kima.intelligentchat.presentation.charactersList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.navigation.navigateToCard
import ru.kima.intelligentchat.presentation.navigation.navigateToImage

@Composable
fun CharactersListScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    viewModel: CharactersListViewModel
) {
    val content = LocalContext.current
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        viewModel.onUserEvent(CharactersListUserEvent.AddCardFromImage(imageBytes))
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

                is CharactersListUiEvent.ShowImage ->
                    navController.navigateToImage(event.imageName)

                CharactersListUiEvent.NoCardPhoto -> scope.launch {
                    snackbarHostState.showSnackbar(content.getString(R.string.no_card_photo))
                }

                CharactersListUiEvent.NoSuchCard -> scope.launch {
                    snackbarHostState.showSnackbar(content.getString(R.string.no_such_card))
                }
            }
        }
    }


    var isExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            ActionButtons(isExpanded,
                onImageButtonClick = {
                    viewModel.onUserEvent(CharactersListUserEvent.AddCardFromImageClicked)
                },
                onCreateButtonClick = {
                    viewModel.onUserEvent(CharactersListUserEvent.CreateCardClicked)
                },
                onControlButtonClick = {
                    isExpanded = !isExpanded
                })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CharactersListContent(state) { event ->
                viewModel.onUserEvent(event)
            }
        }
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
        if (isExpanded) {
            val padding = Modifier.padding(bottom = 16.dp)
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

        FloatingActionButton(
            onClick = onControlButtonClick,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isExpanded) {
                Icon(Icons.Filled.Close, contentDescription = "Collapse actions")
            } else {
                Icon(Icons.Filled.Add, contentDescription = "Expand actions")
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