package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SettingItem(
    item: Setting.SettingItem<*>,
    modifier: Modifier = Modifier
) {
    when (item) {
        is Setting.SettingItem.SwitchSetting -> SwitchSetting(item, modifier)
    }
}

@Composable
fun SwitchSetting(
    item: Setting.SettingItem.SwitchSetting,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val checked by item.pref.subscribe().collectAsStateWithLifecycle(false)
    SwitchSettingsItem(
        title = item.title,
        checked = checked,
        onCheckedChange = {
            scope.launch { item.onValueChanged(it) }
        },
        modifier = modifier,
        description = item.subtitle,
        enabled = item.enabled,
        icon = item.icon
    )
}

@Preview
@Composable
private fun SwitchSettingItemPreview() {
    IntelligentChatTheme {
        Surface {
            SettingItem(
                item = Setting.SettingItem.SwitchSetting(
                    pref = object : Preference<Boolean> {
                        override fun subscribe() = flowOf(true)
                        override suspend fun set(value: Boolean) {}
                    },
                    title = "Title",
                    enabled = true,
                    subtitle = "Subtitle",
                    icon = Icons.Default.Settings,
                    onValueChanged = { true }
                )
            )
        }
    }
}