package ru.kima.intelligentchat.presentation.settings.screen.components.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun SwitchSettingWidget(
    title: ComposeString,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: ComposeString? = null,
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
    ICPreview {
        Column {
            SwitchSettingWidget(
                title = ComposeString.Raw("Text setting with icon"),
                checked = true,
                onCheckedChange = {},
                description = ComposeString.Raw("Text setting summary"),
                icon = Icons.Default.Settings
            )
            SwitchSettingWidget(
                title = ComposeString.Raw("Switch setting widget with summary"),
                checked = false,
                onCheckedChange = {},
                description = ComposeString.Raw("Switch setting summary"),
            )
            SwitchSettingWidget(
                title = ComposeString.Raw("Switch setting"),
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}

