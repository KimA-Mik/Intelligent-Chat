package ru.kima.intelligentchat.presentation.settings.screen.components.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun TextSettingWidget(
    title: ComposeString,
    modifier: Modifier = Modifier,
    subtitle: ComposeString? = null,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    widget: @Composable (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = {
            Text(text = title.unwrap())
        },
        modifier = modifier,
        supportingContent = {
            subtitle?.let {
                Text(text = it.unwrap())
            }
        },
        leadingContent = {
            icon?.let {
                Icon(
                    imageVector = it,
                    tint = iconTint,
                    contentDescription = title.unwrap()
                )
            }
        },
        trailingContent = widget
    )
}

@PreviewLightDark
@Composable
private fun TextSettingWidgetPreview() {
    ICPreview {
        Column {
            TextSettingWidget(
                title = ComposeString.Raw("Text setting"),
                subtitle = ComposeString.Raw("Text setting summary"),
            )
            TextSettingWidget(
                title = ComposeString.Raw("Text setting with icon"),
                subtitle = ComposeString.Raw("Text setting summary"),
                icon = Icons.Default.Preview
            )
        }
    }
}