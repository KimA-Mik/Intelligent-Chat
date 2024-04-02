package ru.kima.intelligentchat.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val state = rememberTooltipState()
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Surface(
                color = TooltipDefaults.plainTooltipContainerColor,
                shape = TooltipDefaults.plainTooltipContainerShape
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(8.dp),
                    color = TooltipDefaults.plainTooltipContentColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        state = state,
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                scope.launch { state.show() }
            }
        ) {
            Icon(imageVector = icon, contentDescription = text)
        }
    }
}

@Preview
@Composable
private fun TooltipIconButtonPreview() {
    IntelligentChatTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TooltipIconButton(
                icon = Icons.Outlined.QuestionMark,
                text = "Hint",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}