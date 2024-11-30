package ru.kima.intelligentchat.presentation.settings.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.settings.Setting

object AdvancedFormattingScreen : SettingsScreen {
    @Composable
    override fun titleRes() = R.string.settings_nav_item_advanced_formatting_title

    @Composable
    override fun subtitleRes() = R.string.settings_nav_item_advanced_formatting_description

    @Composable
    override fun settings(): ImmutableList<Setting> {
        return persistentListOf()
    }

    override fun icon() = Icons.Default.TextFormat
}