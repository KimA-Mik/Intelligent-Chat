package ru.kima.intelligentchat.presentation.settings.screen

import android.app.UiModeManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearance
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearanceStore
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.setDarkMode
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.settings.screen.components.DarkModeSelector

object AppearanceSettingsScreen : SettingsScreen, KoinComponent {
    @Composable
    override fun titleRes() = R.string.settings_nav_item_appearance_title

    @Composable
    override fun subtitleRes() = R.string.settings_nav_item_appearance_description

    @Composable
    override fun settings(): ImmutableList<Setting> {
        return persistentListOf(
            getThemeSettingGroup()
        )
    }

    override fun icon() = Icons.Default.Palette

    @Composable
    private fun getThemeSettingGroup(): Setting.SettingGroup {
        val appearance = remember { get<AppAppearanceStore>() }
        val uiManager = remember { get<UiModeManager>() }
        val darkMode = appearance.darkMode()
        val darkThemeSelector = Setting.SettingItem.CustomSetting(
            pref = darkMode,
            content = { item, onChange ->
                DarkModeSelector(
                    item = item,
                    onSelectItem = onChange,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            title = "",
            enabled = true,
            onValueChanged = { newValue ->
                uiManager.setDarkMode(newValue)
                true
            }
        )

        val darkModePureBlackSetting = Setting.SettingItem.SwitchSetting(
            pref = appearance.darkModePureBlack(),
            title = stringResource(R.string.appearance_setting_dark_mode_pure_black_title),
            enabled = darkMode.currentValue() != AppAppearance.DarkMode.OFF,
        )

        return Setting.SettingGroup(
            title = stringResource(R.string.appearance_setting_theme_group_title),
            enabled = true,
            settingsItems = persistentListOf(
                darkThemeSelector,
                darkModePureBlackSetting
            )
        )
    }

}