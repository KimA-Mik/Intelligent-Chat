package ru.kima.intelligentchat.presentation.characterCard.charactersList.events

sealed interface CharactersListUserEvent {
    data class EditCardClicked(val cardId: Long) : CharactersListUserEvent
    data class DeleteCardClicked(val cardId: Long) : CharactersListUserEvent
    data class RestoreCardClicked(val cardId: Long) : CharactersListUserEvent
    data object AddCardFromImageClicked : CharactersListUserEvent
    data object CreateCardClicked : CharactersListUserEvent
    data class SearchQueryChanged(val query: String?) : CharactersListUserEvent
    data class ShowCardAvatar(val cardId: Long) : CharactersListUserEvent
    data class AddCardFromImage(val imageBytes: ByteArray) : CharactersListUserEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AddCardFromImage

            return imageBytes.contentEquals(other.imageBytes)
        }

        override fun hashCode(): Int {
            return imageBytes.contentHashCode()
        }
    }

    data class InitDialogValueChanged(val newValue: String) : CharactersListUserEvent
    data object AcceptInitialPersonaName : CharactersListUserEvent
    data object DismissInitialPersonaName : CharactersListUserEvent
    data object OnMenuButtonClicked : CharactersListUserEvent
    data class OpenCardChat(val cardId: Long) : CharactersListUserEvent
}