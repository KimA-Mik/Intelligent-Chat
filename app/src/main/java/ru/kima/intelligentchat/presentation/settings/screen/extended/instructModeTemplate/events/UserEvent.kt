package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events

sealed interface UserEvent {
    data class SelectTemplate(val id: Long) : UserEvent
}