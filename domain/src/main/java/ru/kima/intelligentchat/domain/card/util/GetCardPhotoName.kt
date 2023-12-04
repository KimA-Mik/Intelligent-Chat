package ru.kima.intelligentchat.domain.card.util

import ru.kima.intelligentchat.domain.card.model.CharacterCard

fun getCardPhotoName(card: CharacterCard) = "avatar-${card.id}.png"