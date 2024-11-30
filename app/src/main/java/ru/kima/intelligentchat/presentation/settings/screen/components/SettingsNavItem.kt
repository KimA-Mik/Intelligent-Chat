package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme


@Composable
fun SettingsNavItem(
    title: String,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier = modifier.clickable { onNavigate() },
        supportingContent = {
            description?.let {
                Text(text = it)
            }
        },
        leadingContent = {
            icon?.let {
                Icon(
                    it, contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Preview
@Composable
private fun SettingsNavItemPreview() {
    IntelligentChatTheme {
        Surface {
            SettingsNavItem(
                title = "Title",
                onNavigate = {},
                modifier = Modifier.padding(8.dp),
                description = "Description",
                icon = Icons.Default.Settings
            )
        }
    }
}