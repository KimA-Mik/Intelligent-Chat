package ru.kima.intelligentchat.presentation.characterCard.cardDetails.events

import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsViewModel

sealed interface CardDetailUserEvent {
    data class UpdateCardImage(val bytes: ByteArray) : CardDetailUserEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UpdateCardImage

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    data object SelectImageClicked : CardDetailUserEvent
    data object SaveCard : CardDetailUserEvent
    data class FieldUpdate(val field: CardDetailsViewModel.CardField, val updatedString: String) :
        CardDetailUserEvent

    data object DeleteCardClicked : CardDetailUserEvent
    data object ConfirmDeleteCard : CardDetailUserEvent
    data object OpenAltGreetingsSheet : CardDetailUserEvent
    data object CloseAltGreetingsSheet : CardDetailUserEvent
    data object CreateAltGreeting : CardDetailUserEvent
    data class DeleteAltGreeting(val id: Long) : CardDetailUserEvent
    data object ConfirmDeleteAltGreeting : CardDetailUserEvent
    data class EditAltGreeting(val id: Long) : CardDetailUserEvent
    data object AcceptAltGreetingEdit : CardDetailUserEvent
    data object RejectAltGreetingEdit : CardDetailUserEvent
}