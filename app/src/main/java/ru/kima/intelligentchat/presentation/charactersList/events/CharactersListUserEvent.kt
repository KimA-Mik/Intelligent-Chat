package ru.kima.intelligentchat.presentation.charactersList.events

sealed interface CharactersListUserEvent {
    data class CardSelected(val cardId: Long) : CharactersListUserEvent
    data object AddCardFromImageClicked : CharactersListUserEvent
    data object CreateCardClicked : CharactersListUserEvent
    data class SearchQueryChanged(val query: String) : CharactersListUserEvent
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
}