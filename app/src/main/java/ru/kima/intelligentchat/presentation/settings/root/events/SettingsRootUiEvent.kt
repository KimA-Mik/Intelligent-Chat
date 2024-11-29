package ru.kima.intelligentchat.presentation.settings.root.events

sealed interface SettingsRootUiEvent {
    data object NotImplemented : SettingsRootUiEvent
    data object NavigateToChatSettings : SettingsRootUiEvent
}