package ru.kima.intelligentchat.presentation.chat.chatScreen.events

sealed interface UserEvent {
    data class UpdateInputMessage(val message: String) : UserEvent
}