package ru.kima.intelligentchat.presentation.cardDetails

import ru.kima.intelligentchat.domain.model.CharacterCard

data class CardDetailsState(
    val card: CharacterCard = CharacterCard()
)