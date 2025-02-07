package ru.kima.intelligentchat.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.domain.preferences.Preference

sealed interface Setting {
    val title: ComposeString
    val enabled: Boolean

    sealed interface SettingItem<T> : Setting {
        val subtitle: ComposeString?
        val icon: ImageVector?
        val onValueChanged: suspend (newValue: T) -> Boolean

        data class LabelSetting(
            override val title: ComposeString,
            override val enabled: Boolean = true,
            override val subtitle: ComposeString? = null,
            override val icon: ImageVector? = null,
            override val onValueChanged: suspend (Unit) -> Boolean = { true },
        ) : SettingItem<Unit>

        data class SwitchSetting(
            val pref: Preference<Boolean>,
            override val title: ComposeString,
            override val enabled: Boolean = true,
            override val subtitle: ComposeString? = null,
            override val icon: ImageVector? = null,
            override val onValueChanged: suspend (newValue: Boolean) -> Boolean = { true },
        ) : SettingItem<Boolean>

        data class CustomSetting<T>(
            val pref: Preference<T>,
            val content: @Composable (value: T, settingUpdate: (T) -> Unit) -> Unit,
            override val title: ComposeString,
            override val enabled: Boolean = true,
            override val subtitle: ComposeString? = null,
            override val icon: ImageVector? = null,
            override val onValueChanged: suspend (newValue: T) -> Boolean = { true }
        ) : SettingItem<T>
    }

    data class SettingGroup(
        override val title: ComposeString,
        override val enabled: Boolean,

        val settingsItems: ImmutableList<SettingItem<*>>
    ) : Setting
}