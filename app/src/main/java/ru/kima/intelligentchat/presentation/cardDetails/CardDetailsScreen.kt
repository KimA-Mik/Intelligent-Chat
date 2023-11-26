package ru.kima.intelligentchat.presentation.cardDetails

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardDetailsScreen(
    navController: NavController,
    viewModel: CardDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val imagePicker = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.loadCardAvatar(uri)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                UiEvent.SelectImage -> {
                    imagePicker.launch("image/*")
                }

                is UiEvent.SnackbarMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            uiEvent.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                CardImage(photoFilePath = state.card.photoFilePath) {
                    viewModel.onSelectImageClicked()
                }
                Text(
                    text = state.card.name,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f)
                )
            }

            Text(
                text = state.card.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Preview(name = "Card details light mode")
@Preview(
    name = "Card details dark mode",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewCardDetails() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            val savedStateHandle = SavedStateHandle()
            savedStateHandle["cardTitle"] = "Title"
            savedStateHandle["cardDescription"] = "Description"
            CardDetailsScreen(rememberNavController(), CardDetailsViewModel(savedStateHandle))
        }
    }
}