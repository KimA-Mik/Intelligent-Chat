package ru.kima.intelligentchat.presentation.settings.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import ru.kima.intelligentchat.presentation.settings.Setting

interface SettingsScreen {
    @Composable
    @ReadOnlyComposable
    fun titleRes(): Int

    @Composable
    @ReadOnlyComposable
    fun subtitleRes(): Int?

    @Composable
    fun settings(): ImmutableList<Setting>

    fun icon(): ImageVector?
}