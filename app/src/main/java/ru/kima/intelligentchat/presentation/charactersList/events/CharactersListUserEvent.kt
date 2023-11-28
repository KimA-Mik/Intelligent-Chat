package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUserEvent {
    data class CardSelected(val cardId: Int) : CharactersListUserEvent

}