package ru.kima.intelligentchat.presentation.cardDetails.events

sealed interface UiEvent {
    data object SelectImage : UiEvent
    class SnackbarMessage(val message: String) : UiEvent
}