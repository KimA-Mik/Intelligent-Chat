package ru.kima.intelligentchat.presentation.charactersList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.charactersList.components.CardItem

@Composable
fun CharactersListScreen(viewModel: CharactersListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.state.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        items(state.cards) { card ->
            CardItem(
                card = card,
                modifier = Modifier.padding()
            )
        }
    }
}