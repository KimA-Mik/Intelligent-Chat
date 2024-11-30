package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.android.preferences.ICPreference
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.settings.util.collectAsState
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun <T> SettingItem(
    item: Setting.SettingItem<T>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    when (item) {
        is Setting.SettingItem.SwitchSetting -> SwitchSetting(
            item = item,
            onCheckedChange = {
                scope.launch { item.onValueChanged(it) }
            },
            modifier = modifier
        )

        is Setting.SettingItem.CustomSetting -> CustomSetting(
            item = item,
            onChange = {
                scope.launch { item.onValueChanged(it) }
            },
            modifier = modifier
        )
    }
}

@Composable
fun SwitchSetting(
    item: Setting.SettingItem.SwitchSetting,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val checked by item.pref.collectAsState()
    SwitchSettingsItem(
        title = item.title,
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        description = item.subtitle,
        enabled = item.enabled,
        icon = item.icon
    )
}

@Composable
fun <T> CustomSetting(
    item: Setting.SettingItem.CustomSetting<T>,
    onChange: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val pref by item.pref.collectAsState()
    Box(modifier = modifier) {
        item.content(pref, onChange)
    }
}

@Preview
@Composable
private fun SwitchSettingItemPreview() {
    IntelligentChatTheme {
        Surface {
            SettingItem(
                item = Setting.SettingItem.SwitchSetting(
                    pref = ICPreference(
                        initialValue = true,
                        flow = flowOf(true),
                        setter = {}
                    ),
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