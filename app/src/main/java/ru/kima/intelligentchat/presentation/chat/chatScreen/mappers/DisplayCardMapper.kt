package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

fun CharacterCard.toDisplayCard(): DisplayCard {
    return DisplayCard(
        name = name,
        image = ImmutableBitmap(photoBytes)
    )
}