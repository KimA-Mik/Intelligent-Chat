package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableChatAppearance(
    val showDate: Boolean,
    val showNumber: Boolean,
    val showImages: Boolean = false,
    val showImagesOverride: Boolean? = null,
) {
    companion object {
        val default =
            ImmutableChatAppearance(showDate = true, showNumber = true, showImages = false)
    }
}
