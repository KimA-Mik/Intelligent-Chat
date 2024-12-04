package ru.kima.intelligentchat.presentation.settings.root.events

sealed interface SettingsRootUiEvent {
    data object PopBack : SettingsRootUiEvent
    data object NotImplemented : SettingsRootUiEvent
}