package ru.kima.intelligentchat.presentation.settings.events

sealed interface SettingsRootAction {
    data object OpenChatAppearance : SettingsRootAction
    data object OpenAdvancedFormatting : SettingsRootAction
}