package ru.kima.intelligentchat.presentation.chat.cardChatList.events

sealed interface UserEvent {
    data class SelectChat(val chatId: Long) : UserEvent
    data object CreateChat : UserEvent
}