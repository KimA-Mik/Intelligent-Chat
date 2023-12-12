package ru.kima.intelligentchat.data.card.util

import ru.kima.intelligentchat.domain.card.model.CharacterCard

fun getCardPhotoName(card: CharacterCard) = "avatar-${card.id}.png"