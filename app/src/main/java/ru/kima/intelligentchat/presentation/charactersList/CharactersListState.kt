package ru.kima.intelligentchat.presentation.charactersList

data class CharactersListState(
    val cards: List<ru.kima.intelligentchat.domain.card.model.CharacterCard> = emptyList(),
    val searchText: String = ""
)
