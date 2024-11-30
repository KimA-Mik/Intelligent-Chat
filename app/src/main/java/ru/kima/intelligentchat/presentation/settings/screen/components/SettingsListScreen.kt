package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.presentation.settings.Setting
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SettingsListScreen(
    settings: ImmutableList<Setting>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(settings) { setting ->
            when (setting) {
                is Setting.SettingGroup -> {
                    SettingGroup(setting)
                    if (setting != settings.last()) {
                        Spacer(Modifier.height(12.dp))
                    }
                }

                is Setting.SettingItem<*> -> SettingItem(setting)
            }
        }
    }
}

@Preview
@Composable
private fun SettingsListScreenPreview() {
    val group = Setting.SettingGroup(
        title = "Group",
        enabled = true,
        settingsItems = persistentListOf(
            Setting.SettingItem.SwitchSetting(
                pref = object : Preference<Boolean> {
                    override fun subscribe() = flowOf(true)
                    override suspend fun set(value: Boolean) {}
                },
                title = "One"
            ),
            Setting.SettingItem.SwitchSetting(
                pref = object : Preference<Boolean> {
                    override fun subscribe() = flowOf(false)
                    override suspend fun set(value: Boolean) {}
                },
                title = "Two"
            ),
        )
    )

    IntelligentChatTheme {
        Surface {
            SettingsListScreen(
                settings = persistentListOf(
                    group
                ),
            )
        }
    }
}
