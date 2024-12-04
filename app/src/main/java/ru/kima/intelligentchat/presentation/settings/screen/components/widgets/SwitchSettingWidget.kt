package ru.kima.intelligentchat.presentation.settings.screen.components.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SwitchSettingWidget(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
) {
    TextSettingWidget(
        title = title,
        modifier = modifier.clickable { onCheckedChange(!checked) },
        subtitle = description,
        icon = icon,
        widget = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    )
}

@PreviewLightDark
@Composable
private fun SwitchSettingsItemPreview() {
    IntelligentChatTheme {
        Surface {
            Column {
                SwitchSettingWidget(
                    title = "Text setting with icon",
                    checked = true,
                    onCheckedChange = {},
                    description = "Text setting summary",
                    icon = Icons.Default.Settings
                )
                SwitchSettingWidget(
                    title = "Switch setting widget with summary",
                    checked = false,
                    onCheckedChange = {},
                    description = "Switch setting summary",
                )
                SwitchSettingWidget(
                    title = "Switch setting",
                    checked = false,
                    onCheckedChange = {},
                )
            }
        }
    }
}

