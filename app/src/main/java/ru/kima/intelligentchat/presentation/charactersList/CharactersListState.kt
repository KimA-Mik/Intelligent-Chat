package ru.kima.intelligentchat.presentation.charactersList

import ru.kima.intelligentchat.domain.card.model.CharacterCard

data class CharactersListState(
    val cards: List<CharacterCard> = emptyList(),
    val searchText: String = ""
)
