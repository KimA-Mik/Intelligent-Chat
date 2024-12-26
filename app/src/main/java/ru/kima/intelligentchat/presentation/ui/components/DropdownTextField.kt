package ru.kima.intelligentchat.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun DropdownTextField(
    value: ComposeString,
    variants: ImmutableList<SimpleDropDownMenuItem>,
    modifier: Modifier = Modifier,
    label: ComposeString? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    Box(modifier.width(IntrinsicSize.Min)) {
        var isMenuExpanded by remember { mutableStateOf(false) }
        val rotation by animateFloatAsState(
            targetValue = if (isMenuExpanded) 0f else 180f,
            label = "rotation"
        )
        TextField(
            value = value.unwrap(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropUp, contentDescription = "",
                        modifier = Modifier.graphicsLayer(
                            rotationZ = rotation
                        )
                    )
                }
            },
            label = {
                label?.let {
                    Text(it.unwrap())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines
        )
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }) {
            variants.fastForEach { variant ->
                val variantText = variant.string.unwrap()
                DropdownMenuItem(
                    text = { Text(variantText) },
                    onClick = {
                        isMenuExpanded = false
                        variant.onClick()
                    },
                    leadingIcon = {
                        variant.iconVector?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = variantText
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropdownTextFieldPreview() {
    IntelligentChatTheme {
        Surface {
            DropdownTextField(
                value = ComposeString.Raw("String"),
                variants = persistentListOf(),
                modifier = Modifier.fillMaxWidth(),
                label = ComposeString.Raw("Label")
            )
        }
    }
}