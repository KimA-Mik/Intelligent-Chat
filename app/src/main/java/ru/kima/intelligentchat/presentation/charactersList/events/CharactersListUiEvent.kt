package ru.kima.intelligentchat.presentation.charactersList.events

import ru.kima.intelligentchat.R

sealed interface CharactersListUiEvent {
    data class NavigateToCard(val cardId: Long) : CharactersListUiEvent
    data class ShowCardImage(val cardId: Long) : CharactersListUiEvent
    data class SnackbarMessage(val message: String) : CharactersListUiEvent
    data object SelectPngImage : CharactersListUiEvent
    data class SelectPersona(val id: Long) : CharactersListUiEvent

    enum class Message(val messageId: Int) : CharactersListUiEvent {
        NoSuchCard(R.string.no_such_card),
        NoCardPhoto(R.string.no_card_photo),
        DefaultPersonaInit(R.string.default_name_message)
    }
}