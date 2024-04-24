package ru.kima.intelligentchat.presentation.chat.chatScreen

data class ChatScreenState(
    val selectedPersona: Long = 0,
    val inputMessageBuffer: String = String(),
)
