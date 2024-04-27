package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class DisplayChat(
    val messages: List<DisplayMessage> = emptyList()
)
