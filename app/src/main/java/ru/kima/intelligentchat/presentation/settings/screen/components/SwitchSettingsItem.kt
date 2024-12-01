package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.common.components.conditional
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun SwitchSettingsItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier = modifier.conditional(enabled) {
            clickable { onCheckedChange(!checked) }
        },
        supportingContent = {
            description?.let {
                Text(text = it)
            }
        },
        leadingContent = {
            icon?.let {
                Icon(it, contentDescription = title)
            }
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
            )
        }
    )
}

@Preview
@Composable
private fun SwitchSettingsItemPreview() {
    IntelligentChatTheme {
        Surface {
            SwitchSettingsItem(
                title = "Title",
                checked = true,
                onCheckedChange = {},
                modifier = Modifier.padding(8.dp),
                description = "Description",
            )
        }
    }
}