package ru.kima.intelligentchat.presentation.settings.screen.components.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.settings.util.collectAsState

@Composable
fun <T> RootWidget(
    item: Setting.SettingItem<T>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    when (item) {
        is Setting.SettingItem.SwitchSetting -> {
            val checked by item.pref.collectAsState()
            SwitchSettingWidget(
                title = item.title,
                checked = checked,
                onCheckedChange = {
                    scope.launch { item.onValueChanged(it) }
                },
                modifier = modifier,
                description = item.subtitle,
                icon = item.icon
            )
        }

        is Setting.SettingItem.CustomSetting -> {
            val pref by item.pref.collectAsState()
            Box(modifier = modifier) {
                item.content(pref) {
                    scope.launch { item.onValueChanged(it) }
                }
            }
        }
    }
}