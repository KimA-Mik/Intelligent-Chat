package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUiEvent {
    data class NavigateTo(val cardId: Int) : CharactersListUiEvent
}