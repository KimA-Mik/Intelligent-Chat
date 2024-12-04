package ru.kima.intelligentchat.presentation.settings.screen.components.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun TextSettingWidget(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    widget: @Composable (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier = modifier,
        supportingContent = {
            subtitle?.let {
                Text(text = it)
            }
        },
        leadingContent = {
            icon?.let {
                Icon(
                    imageVector = it,
                    tint = iconTint,
                    contentDescription = title
                )
            }
        },
        trailingContent = widget
    )
}

@PreviewLightDark
@Composable
private fun TextSettingWidgetPreview() {
    IntelligentChatTheme {
        Surface {
            Column {
                TextSettingWidget(
                    title = "Text setting",
                    subtitle = "Text setting summary",
                )
                TextSettingWidget(
                    title = "Text setting with icon",
                    subtitle = "Text setting summary",
                    icon = Icons.Default.Preview
                )
            }
        }
    }
}