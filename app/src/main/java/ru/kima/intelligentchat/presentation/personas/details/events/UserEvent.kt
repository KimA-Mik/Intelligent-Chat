package ru.kima.intelligentchat.presentation.personas.details.events

import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel

sealed interface UserEvent {
    data class UpdatePersonaDetailsField(
        val field: PersonaDetailsViewModel.PersonaDetailsField,
        val value: String
    ) : UserEvent

    data object SavePersona : UserEvent

    data class UpdatePersonaImage(val bytes: ByteArray) : UserEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UpdatePersonaImage

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }
}