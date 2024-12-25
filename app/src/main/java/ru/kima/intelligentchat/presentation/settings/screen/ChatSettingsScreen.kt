package ru.kima.intelligentchat.presentation.settings.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository
import ru.kima.intelligentchat.presentation.settings.Setting

object ChatSettingsScreen : SettingsScreen, KoinComponent {
    @Composable
    override fun titleRes() = R.string.settings_nav_item_chat_settings_title

    @Composable
    override fun subtitleRes() = R.string.settings_nav_item_chat_settings_description

    @Composable
    override fun settings(): ImmutableList<Setting> {
        val settings = remember { get<ChatSettingsRepository>() }

        val showNumber = settings.showNumber()
        val showDate = settings.showDate()
        val appearanceGroup = Setting.SettingGroup(
            title = ComposeString.Resource(R.string.appearance_setting_group_title),
            enabled = true,
            settingsItems = persistentListOf(
                Setting.SettingItem.SwitchSetting(
                    pref = showNumber,
                    title = ComposeString.Resource(R.string.setting_show_message_number_title),
                    onValueChanged = {
                        showNumber.set(it)
                        true
                    }
                ),
                Setting.SettingItem.SwitchSetting(
                    pref = showDate,
                    title = ComposeString.Resource(R.string.setting_show_swipe_date_title),
                    onValueChanged = {
                        showDate.set(it)
                        true
                    }
                )
            )
        )

        return persistentListOf(
            appearanceGroup
        )
    }

    override fun icon() = Icons.AutoMirrored.Filled.Chat
}