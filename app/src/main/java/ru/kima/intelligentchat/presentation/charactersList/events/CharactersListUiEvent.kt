package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUiEvent {
    data class NavigateTo(val cardId: Long) : CharactersListUiEvent
    data class SnackbarMessage(val message: String) : CharactersListUiEvent
    data object SelectPngImage : CharactersListUiEvent
}