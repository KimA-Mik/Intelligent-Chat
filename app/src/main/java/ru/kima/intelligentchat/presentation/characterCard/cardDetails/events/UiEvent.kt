package ru.kima.intelligentchat.presentation.characterCard.cardDetails.events

sealed interface UiEvent {
    data object SelectImage : UiEvent
    class SnackbarMessage(val message: String) : UiEvent
    data object PopBack : UiEvent
}