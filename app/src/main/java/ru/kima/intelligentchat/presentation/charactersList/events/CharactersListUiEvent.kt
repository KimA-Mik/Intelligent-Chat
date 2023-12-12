package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUiEvent {
    data class NavigateToCard(val cardId: Long) : CharactersListUiEvent
    data class ShowCardImage(val cardId: Long) : CharactersListUiEvent
    data class SnackbarMessage(val message: String) : CharactersListUiEvent
    data object SelectPngImage : CharactersListUiEvent
    data object NoSuchCard : CharactersListUiEvent
    data object NoCardPhoto : CharactersListUiEvent
}