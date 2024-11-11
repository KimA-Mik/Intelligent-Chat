package ru.kima.intelligentchat.presentation.chat.cardChatList.events

sealed interface UserEvent {
    data class SelectChat(val chatId: Long) : UserEvent
    data class RenameChat(val chatId: Long) : UserEvent
    data class DeleteChat(val chatId: Long) : UserEvent
    data object DeleteChatAccept : UserEvent
    data object DeleteChatDismiss : UserEvent
    data object CreateChat : UserEvent
}