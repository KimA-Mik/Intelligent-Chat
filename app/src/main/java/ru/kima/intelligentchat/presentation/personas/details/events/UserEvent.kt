package ru.kima.intelligentchat.presentation.personas.details.events

import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel

sealed interface UserEvent {
    data class UpdatePersonaDetailsField(
        val field: PersonaDetailsViewModel.PersonaDetailsField,
        val value: String
    ) : UserEvent

    data object SavePersona : UserEvent
}