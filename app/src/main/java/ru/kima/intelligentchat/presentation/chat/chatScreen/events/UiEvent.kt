package ru.kima.intelligentchat.presentation.chat.chatScreen.events

sealed interface UiEvent {
    data object OpenChatList : UiEvent
}