package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.flowOf
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.android.preferences.ICPreference
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.settings.screen.components.widgets.RootWidget
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun <T> SettingItem(
    item: Setting.SettingItem<T>,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        item.enabled
    ) {
        RootWidget(item, modifier)
    }
}


@Preview
@Composable
private fun SwitchSettingItemPreview() {
    ICPreview {
        SettingItem(
            item = Setting.SettingItem.SwitchSetting(
                pref = ICPreference(
                    initialValue = true,
                    flow = flowOf(true),
                    setter = {}
                ),
                title = ComposeString.Raw("Title"),
                enabled = true,
                subtitle = ComposeString.Raw("Subtitle"),
                icon = Icons.Default.Settings,
                onValueChanged = { true }
            )
        )

    }
}