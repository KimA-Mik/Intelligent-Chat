package ru.kima.intelligentchat.presentation.settings.root.events

import ru.kima.intelligentchat.presentation.settings.screen.SettingsScreen

sealed interface SettingsRootAction {


    data class SelectScreen(val screen: SettingsScreen) : SettingsRootAction
    data object ClearScreen : SettingsRootAction
}