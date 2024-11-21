package ru.kima.intelligentchat.presentation.characterCard.charactersList

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry

@Immutable
data class CharactersListState(
    val cards: ImmutableList<ImmutableCardEntry> = persistentListOf(),
    val searchText: String? = null,
    val initialDialog: Boolean = false,
    val initialDialogText: String = String()
)
