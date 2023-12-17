package ru.kima.intelligentchat.data.card.mappers

import ru.kima.intelligentchat.data.card.entities.AltGreetingEntity
import ru.kima.intelligentchat.domain.card.model.AltGreeting

fun AltGreetingEntity.toAltGreeting(): AltGreeting {
    return AltGreeting(
        id = id,
        body = body
    )
}

fun AltGreeting.toEntity(cardId: Long): AltGreetingEntity {
    return AltGreetingEntity(
        id = id,
        cardId = cardId,
        body = body
    )
}
