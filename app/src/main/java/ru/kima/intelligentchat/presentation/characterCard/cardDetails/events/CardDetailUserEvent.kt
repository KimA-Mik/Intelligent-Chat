package ru.kima.intelligentchat.presentation.characterCard.cardDetails.events

import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsViewModel

sealed interface CardDetailUserEvent {
    data class UpdateCardImage(val bytes: ByteArray) : CardDetailUserEvent
    data object SelectImageClicked : CardDetailUserEvent
    data object SaveCard : CardDetailUserEvent
    data class FieldUpdate(val field: CardDetailsViewModel.CardField, val updatedString: String) :
        CardDetailUserEvent

    data object DeleteCardClicked : CardDetailUserEvent
    data object DeleteCard : CardDetailUserEvent
}