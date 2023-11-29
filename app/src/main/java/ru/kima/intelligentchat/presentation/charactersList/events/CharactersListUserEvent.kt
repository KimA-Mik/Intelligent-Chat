package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUserEvent {
    data class CardSelected(val cardId: Long) : CharactersListUserEvent
    data object AddCardFromImageClicked : CharactersListUserEvent
    data class AddCardFromImage(val imageBytes: ByteArray) : CharactersListUserEvent
}