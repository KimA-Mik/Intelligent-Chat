package ru.kima.intelligentchat.presentation.chat.cardChatList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UiEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UserEvent
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardChatListScreen(
    state: CardChatListState,
    uiEvent: Event<UiEvent>,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onEvent: (UserEvent) -> Unit
) {
    val popBackStack = remember<() -> Unit> {
        {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.displayCard.image.imageBitmap?.let {
                            CardImage(
                                bitmap = state.displayCard.image,
                                modifier = Modifier.size(40.dp),
                                onClick = {}
                            )
                        }
                        Text(
                            text = state.displayCard.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )

        }
    ) { paddingValues ->
        CardChatListScreenContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun CardChatListScreenContent(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun CardChatListScreenPreview() {
    IntelligentChatTheme {
        CardChatListScreen(
            state = CardChatListState(),
            uiEvent = Event(null),
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            modifier = Modifier.fillMaxSize(),
            onEvent = {}
        )
    }
}