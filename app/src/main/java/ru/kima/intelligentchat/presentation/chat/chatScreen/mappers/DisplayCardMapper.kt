package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard

fun CharacterCard.toDisplayCard(): DisplayCard {
    return DisplayCard(
        id = id,
        name = name,
        photoName = photoName,
    )
}