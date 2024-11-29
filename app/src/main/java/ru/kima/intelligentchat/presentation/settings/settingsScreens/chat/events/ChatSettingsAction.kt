package ru.kima.intelligentchat.presentation.settings.settingsScreens.chat.events

sealed interface ChatSettingsAction {
    data class SetShowDate(val value: Boolean) : ChatSettingsAction
    data class SetShowNumber(val value: Boolean) : ChatSettingsAction
}