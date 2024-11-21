package ru.kima.intelligentchat.presentation.characterCard.charactersList

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry

@Immutable
data class CharactersListState(
    val cards: List<ImmutableCardEntry> = emptyList(),
    val searchText: String? = null,
    val initialDialog: Boolean = false,
    val initialDialogText: String = String()
)
