package ru.kima.intelligentchat.presentation.chat.chatScreen.events

sealed interface UserEvent {
    data class UpdateInputMessage(val message: String) : UserEvent
    data class MessageSwipeLeft(val messageId: Long) : UserEvent
    data class MessageSwipeRight(val messageId: Long) : UserEvent
    data class DeleteMessage(val messageId: Long) : UserEvent
    data class EditMessage(val messageId: Long) : UserEvent
    data object SaveEditedMessage : UserEvent
    data object DismissEditedMessage : UserEvent
    data class UpdateEditedMessage(val text: String) : UserEvent
    data object MessageButtonClicked : UserEvent
    data class MoveMessageUp(val messageId: Long) : UserEvent
    data class MoveMessageDown(val messageId: Long) : UserEvent
    data object OpenChatList : UserEvent
    data class BranchFromMessage(val messageId: Long) : UserEvent
    data class RestoreMessage(val messageId: Long) : UserEvent
    data object ScrollDown : UserEvent
    data class DeleteCurrentSwipe(val messageId: Long) : UserEvent
}