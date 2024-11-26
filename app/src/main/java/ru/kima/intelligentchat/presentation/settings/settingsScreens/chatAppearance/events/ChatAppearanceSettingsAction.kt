package ru.kima.intelligentchat.presentation.settings.settingsScreens.chatAppearance.events

sealed interface ChatAppearanceSettingsAction {
    data class SetShowDate(val value: Boolean) : ChatAppearanceSettingsAction
    data class SetShowNumber(val value: Boolean) : ChatAppearanceSettingsAction
}