package ru.kima.intelligentchat.presentation.settings.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearance
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun DarkModeSelector(
    item: AppAppearance.DarkMode,
    onSelectItem: (AppAppearance.DarkMode) -> Unit,
    modifier: Modifier = Modifier,
) = Surface(
    modifier = modifier.height(IntrinsicSize.Min),
    shape = MaterialTheme.shapes.extraLarge,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAppearance.DarkMode.entries.fastForEachIndexed { i, entry ->
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelectItem(entry) },
                color = if (entry == item) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
            ) {
                val textId = when (entry) {
                    AppAppearance.DarkMode.SYSTEM -> R.string.appearance_setting_dark_theme_system
                    AppAppearance.DarkMode.ON -> R.string.appearance_setting_dark_theme_on
                    AppAppearance.DarkMode.OFF -> R.string.appearance_setting_dark_theme_off
                }

                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .animateContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        entry == item
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(text = stringResource(textId))
                }
            }

            if (i < AppAppearance.DarkMode.entries.lastIndex) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Preview
@Composable
private fun SwitchSettingItemPreview() {
    IntelligentChatTheme {
        Surface {
            DarkModeSelector(
                AppAppearance.DarkMode.SYSTEM,
                onSelectItem = {},
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}