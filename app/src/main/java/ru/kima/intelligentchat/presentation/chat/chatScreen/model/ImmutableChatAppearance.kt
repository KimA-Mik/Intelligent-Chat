package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableChatAppearance(
    val showDate: Boolean,
    val showNumber: Boolean,
) {
    companion object {
        val default = ImmutableChatAppearance(showDate = true, showNumber = true)
    }
}
