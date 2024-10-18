package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class DisplayChat(
    val chatId: Long = 0,
    val selectedPersonaId: Long = 0,
    val messages: List<DisplayMessage> = emptyList()
)
