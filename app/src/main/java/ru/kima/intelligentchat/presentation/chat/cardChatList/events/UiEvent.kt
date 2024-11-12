package ru.kima.intelligentchat.presentation.chat.cardChatList.events

sealed interface UiEvent {
    data object IncorrectInitialization : UiEvent
}