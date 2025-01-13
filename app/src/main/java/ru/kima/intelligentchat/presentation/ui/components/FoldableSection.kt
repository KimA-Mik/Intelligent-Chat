package ru.kima.intelligentchat.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun FoldableSection(
    title: @Composable RowScope.() -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    titleVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = titleVerticalAlignment,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                title()
            }

            IconButton(
                onClick = {
                    onExpandedChange(!expanded)
                }
            ) {
                Icon(
                    modifier = Modifier.graphicsLayer(
                        rotationZ = rotation
                    ),
                    imageVector = Icons.Filled.ArrowDropUp,
                    contentDescription = ""
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            content()
        }
    }
}

@Preview
@Composable
private fun FoldableSectionPreview() = ICPreview {
    FoldableSection(
        title = {
            Text(text = "Title")
        },
        expanded = true,
        onExpandedChange = {},
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Section")
    }
}