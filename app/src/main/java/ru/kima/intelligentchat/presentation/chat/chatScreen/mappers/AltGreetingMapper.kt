package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.chat.model.Swipe

fun AltGreeting.toSwipe(messageId: Long): Swipe {
    return Swipe(
        messageId = messageId,
        text = body
    )
}