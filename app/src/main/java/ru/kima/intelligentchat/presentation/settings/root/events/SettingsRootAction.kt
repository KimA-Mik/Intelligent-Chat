package ru.kima.intelligentchat.presentation.settings.root.events

sealed interface SettingsRootAction {
    data object OpenChatSettings : SettingsRootAction
    data object OpenAdvancedFormatting : SettingsRootAction
}