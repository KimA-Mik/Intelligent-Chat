package ru.kima.intelligentchat.presentation.chat.chatScreen.events

sealed interface UiEvent {
    data object OpenChatList : UiEvent
    data class RestoreMessage(val messageId: Long) : UiEvent
    data object ScrollDown : UiEvent
    data class RestoreSwipe(val messageId: Long, val swipeId: Long) : UiEvent
    data class OpenUri(val uri: String) : UiEvent
}