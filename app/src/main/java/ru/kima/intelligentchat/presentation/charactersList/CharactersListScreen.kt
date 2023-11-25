package ru.kima.intelligentchat.presentation.charactersList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardsUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.PutCardUseCase
import ru.kima.intelligentchat.presentation.charactersList.components.CardItem
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CharactersListScreen(
    navController: NavController,
    viewModel: CharactersListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        items(state.cards) { card ->
            CardItem(
                card = card,
                modifier = Modifier.padding()
            ) {
                navController.navigate("cards/${card.id}")
            }
        }
    }
}

@Preview()
@Composable
fun PreviewCharactersListScreen() {
    KoinApplication(application = {
        modules(module {
            single { GetCardsUseCase() }
            single { GetCardUseCase() }
            single { PutCardUseCase() }
            val cards = List(100) { index ->
                CharacterCard(name = "Name $index", description = "Description $index")
            }
            val savedStateHandle = SavedStateHandle()
            savedStateHandle["cards"] = cards
            viewModel { CharactersListViewModel(savedStateHandle) }
        })
    }) {


        IntelligentChatTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CharactersListScreen(
                    rememberNavController()
                )
            }
        }
    }
}