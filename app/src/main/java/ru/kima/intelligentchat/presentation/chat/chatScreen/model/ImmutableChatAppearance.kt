package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableChatAppearance(
    val showDate: Boolean = true,
    val showNumber: Boolean = true,
)
