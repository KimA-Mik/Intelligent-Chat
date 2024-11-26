package ru.kima.intelligentchat.presentation.settings.events

sealed interface SettingsRootUiEvent {
    data object NotImplemented : SettingsRootUiEvent
}