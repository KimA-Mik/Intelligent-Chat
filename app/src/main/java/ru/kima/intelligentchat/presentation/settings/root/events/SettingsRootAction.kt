package ru.kima.intelligentchat.presentation.settings.root.events

sealed interface SettingsRootAction {
    data object OpenChatAppearance : SettingsRootAction
    data object OpenAdvancedFormatting : SettingsRootAction
}