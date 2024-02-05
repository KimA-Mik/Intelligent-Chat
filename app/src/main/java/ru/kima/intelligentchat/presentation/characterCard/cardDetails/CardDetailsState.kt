package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard

@Immutable
data class CardDetailsState(
    val card: ImmutableCard = ImmutableCard(),
    val deleteCardDialog: Boolean = false,
    val showAltGreeting: Boolean = false,
    val deleteAltGreetingDialog: Boolean = false,
    val editableGreeting: Long = 0,
    val editableGreetingBuffer: String = String()
)