package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettings
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableChatAppearance

fun ChatSettings.toImmutable(): ImmutableChatAppearance {
    return ImmutableChatAppearance(
        showDate = showDate,
        showNumber = showNumber,
    )
}