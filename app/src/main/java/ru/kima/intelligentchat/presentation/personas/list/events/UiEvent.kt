package ru.kima.intelligentchat.presentation.personas.list.events

sealed interface UiEvent {
    data class NavigateToPersona(val id: Long) : UiEvent
}