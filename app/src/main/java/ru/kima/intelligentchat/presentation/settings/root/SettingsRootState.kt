package ru.kima.intelligentchat.presentation.settings.root

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.presentation.settings.screen.SettingsScreen

data class SettingsRootState(
    val screens: ImmutableList<SettingsScreen> = persistentListOf(),
    val selectedScreen: SettingsScreen? = null
)
