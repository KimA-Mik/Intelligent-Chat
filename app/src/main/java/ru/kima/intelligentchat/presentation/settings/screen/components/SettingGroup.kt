package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import ru.kima.intelligentchat.presentation.settings.Setting

@Composable
fun SettingGroup(
    group: Setting.SettingGroup,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SettingsGroupTitle(
            title = group.title
        )

        group.settingsItems.fastForEach {
            SettingItem(it)
        }
    }
}

