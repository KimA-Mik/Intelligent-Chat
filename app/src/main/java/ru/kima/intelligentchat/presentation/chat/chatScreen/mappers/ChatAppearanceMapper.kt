package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.preferences.chatAppearance.ChatAppearance
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableChatAppearance

fun ChatAppearance.toImmutable(): ImmutableChatAppearance {
    return ImmutableChatAppearance(
        showDate = showDate,
        showNumber = showNumber,
    )
}