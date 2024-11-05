package ru.kima.intelligentchat.presentation.chat.chatScreen.events

sealed interface UserEvent {
    data class UpdateInputMessage(val message: String) : UserEvent
    data class MessageSwipeLeft(val messageId: Long) : UserEvent
    data class MessageSwipeRight(val messageId: Long) : UserEvent
    data class DeleteMessage(val messageId: Long) : UserEvent
    data object MessageButtonClicked : UserEvent
}