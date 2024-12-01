package ru.kima.intelligentchat.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import ru.kima.intelligentchat.domain.preferences.Preference

sealed interface Setting {
    val title: String
    val enabled: Boolean

    sealed interface SettingItem<T> : Setting {
        val subtitle: String?
        val icon: ImageVector?
        val onValueChanged: suspend (newValue: T) -> Boolean

        data class SwitchSetting(
            val pref: Preference<Boolean>,
            override val title: String,
            override val enabled: Boolean = true,
            override val subtitle: String? = null,
            override val icon: ImageVector? = null,
            override val onValueChanged: suspend (newValue: Boolean) -> Boolean = { true },
        ) : SettingItem<Boolean>

        data class CustomSetting<T>(
            val pref: Preference<T>,
            val content: @Composable (T, (T) -> Unit) -> Unit,
            override val title: String,
            override val enabled: Boolean = true,
            override val subtitle: String? = null,
            override val icon: ImageVector? = null,
            override val onValueChanged: suspend (newValue: T) -> Boolean = { true }
        ) : SettingItem<T>
    }

    data class SettingGroup(
        override val title: String,
        override val enabled: Boolean,

        val settingsItems: ImmutableList<SettingItem<*>>
    ) : Setting
}