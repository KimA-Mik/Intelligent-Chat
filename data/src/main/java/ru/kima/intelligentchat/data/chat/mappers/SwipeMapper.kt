package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.domain.chat.model.Swipe

fun SwipeEntity.toSwipe(): Swipe {
    return Swipe(
        swipeId = swipeId,
        messageId = messageId,
        text = text,
        deleted = deleted,
        sendTime = sendTime
    )
}

fun Swipe.toEntity(): SwipeEntity {
    return SwipeEntity(
        swipeId = swipeId,
        messageId = messageId,
        text = text,
        deleted = deleted,
        sendTime = sendTime
    )
}