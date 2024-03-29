package ru.kima.intelligentchat.presentation.personas.list.events

sealed interface UserEvent {
    data class QueryChanged(val query: String) : UserEvent
    data class NavigateToPersona(val id: Long) : UserEvent
    data object CreatePersona : UserEvent
    data class SelectPersona(val id: Long) : UserEvent
}