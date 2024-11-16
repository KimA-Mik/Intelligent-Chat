package ru.kima.intelligentchat.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun AnimatedFab(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enter,
        exit = exit
    ) {
        SmallFloatingActionButton(onClick = onClick) {
            content()
        }
    }
}

@Preview
@Composable
private fun AnimatedFabPreview() {
    IntelligentChatTheme {
        AnimatedFab(
            visible = true,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Image, null)
        }
    }
}