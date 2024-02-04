package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.card.model.CharacterCard

@Immutable
data class CardDetailsState(
    val card: CharacterCard = CharacterCard(),
    val deleteCardDialog: Boolean = false,
    val showAltGreeting: Boolean = false,
    val deleteAltGreetingDialog: Boolean = false,
)