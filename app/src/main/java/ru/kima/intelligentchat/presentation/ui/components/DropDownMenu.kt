package ru.kima.intelligentchat.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

data class SimpleDropDownMenuItem(
    @StringRes
    val textId: Int,
    val onClick: () -> Unit,
    val iconVector: ImageVector? = null
)

@Composable
fun SimpleDropdownMenu(
    menuItems: List<SimpleDropDownMenuItem>,
    modifier: Modifier = Modifier,
    iconVector: ImageVector = Icons.Filled.MoreVert,
) {
    Box(modifier = modifier) {
        var dropdownMenu by remember { mutableStateOf(false) }
        IconButton(onClick = { dropdownMenu = true }) {
            Icon(
                imageVector = iconVector,
                contentDescription = "Dropdown menu",
            )
        }

        DropdownMenu(expanded = dropdownMenu, onDismissRequest = { dropdownMenu = false }) {
            menuItems.forEach { item ->
                val text = stringResource(id = item.textId)
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = item.onClick,
                    leadingIcon = {
                        item.iconVector?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = text
                            )
                        }
                    }
                )
            }
        }
    }
}