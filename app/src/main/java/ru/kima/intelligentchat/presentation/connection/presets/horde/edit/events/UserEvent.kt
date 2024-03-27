package ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events

sealed interface UserEvent {
    data object SavePreset : UserEvent
    data class EditTitle(val newTitle: String) : UserEvent
}