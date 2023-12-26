package ru.kima.intelligentchat.presentation.personas.details.events

sealed interface UserEvent {
    data class UpdatePersonaName(val name: String) : UserEvent
    data class UpdatePersonaDescription(val description: String) : UserEvent
}