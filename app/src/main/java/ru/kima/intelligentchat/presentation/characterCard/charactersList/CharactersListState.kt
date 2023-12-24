package ru.kima.intelligentchat.presentation.characterCard.charactersList

import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.domain.persona.model.Persona

data class CharactersListState(
    val cards: List<CardEntry> = emptyList(),
    val searchText: String = "",
    val persona: Persona = Persona(),
    val initialDialog: Boolean = false,
    val initialDialogText: String = String()
)
