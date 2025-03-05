package ru.kima.intelligentchat.presentation.settings.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToContextTemplate
import ru.kima.intelligentchat.presentation.navigation.graphs.navigateToInstructMode
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.ui.LocalNavController

object AdvancedFormattingScreen : SettingsScreen, KoinComponent {
    @Composable
    override fun titleRes() = R.string.settings_nav_item_advanced_formatting_title

    @Composable
    override fun subtitleRes() = R.string.settings_nav_item_advanced_formatting_description

    @Composable
    override fun settings(): ImmutableList<Setting> {
        val navController = LocalNavController.current
        val contextTemplate = Setting.SettingItem.LabelSetting(
            title = ComposeString.Resource(R.string.context_template_setting_title),
            subtitle = ComposeString.Resource(R.string.context_template_setting_description),
            onValueChanged = {
                navController.navigateToContextTemplate()
                true
            }
        )
        val instructMode = Setting.SettingItem.LabelSetting(
            title = ComposeString.Resource(R.string.instruct_mode_setting_title),
            subtitle = ComposeString.Resource(R.string.instruct_mode_setting_description),
            onValueChanged = {
                navController.navigateToInstructMode()
                true
            }
        )
        return persistentListOf(
            contextTemplate,
            instructMode
        )
    }

    override fun icon() = Icons.Default.TextFormat
}