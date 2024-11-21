package ru.kima.intelligentchat.presentation.characterCard.charactersList.events

import ru.kima.intelligentchat.R

sealed interface CharactersListUiEvent {
    data class NavigateToCardEdit(val cardId: Long) : CharactersListUiEvent
    data class NavigateToCardChat(val cardId: Long) : CharactersListUiEvent
    data class ShowCardImage(val cardId: Long) : CharactersListUiEvent
    data class SnackbarMessage(val message: String) : CharactersListUiEvent
    data object SelectPngImage : CharactersListUiEvent

    enum class Message(val messageId: Int) : CharactersListUiEvent {
        NoSuchCard(R.string.no_such_card),
        NoCardPhoto(R.string.no_card_photo),
        DefaultPersonaInit(R.string.default_name_message)
    }

    data object OpenNavigationDrawer : CharactersListUiEvent
    data class CardDeleted(val cardId: Long) : CharactersListUiEvent
}