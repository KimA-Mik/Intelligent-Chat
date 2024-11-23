package ru.kima.intelligentchat.presentation.characterCard.cardDetails.events

import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.CardField

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
    data class FieldUpdate(val field: CardField, val updatedString: String) :
        CardDetailUserEvent

    data class FieldSwitch(val field: CardField) :
        CardDetailUserEvent

    data object OpenAltGreetingsSheet : CardDetailUserEvent
    data object CloseAltGreetingsSheet : CardDetailUserEvent
    data object CreateAltGreeting : CardDetailUserEvent
    data class DeleteAltGreeting(val id: Long) : CardDetailUserEvent
    data object ConfirmDeleteAltGreeting : CardDetailUserEvent
    data object DismissDeleteAltGreeting : CardDetailUserEvent
    data class EditAltGreeting(val id: Long) : CardDetailUserEvent
    data object AcceptAltGreetingEdit : CardDetailUserEvent
    data object RejectAltGreetingEdit : CardDetailUserEvent
    data class UpdateAlternateGreetingBuffer(val buffer: String) : CardDetailUserEvent
    data object NavigateUp : CardDetailUserEvent
    data class SelectTab(val index: Int) : CardDetailUserEvent
}