package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import ru.kima.intelligentchat.domain.card.model.CharacterCard

data class CardDetailsState(
    val card: CharacterCard = CharacterCard(),
    val showAltGreeting: Boolean = false
)