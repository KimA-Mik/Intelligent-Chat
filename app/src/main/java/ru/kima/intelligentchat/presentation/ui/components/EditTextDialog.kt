package ru.kima.intelligentchat.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.MAX_TEMPLATE_TITLE_LINES
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun EditTextDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = false,
    icon: ImageVector? = null,
    title: ComposeString? = null,
    textFieldLabel: ComposeString? = null
) {
    AlertDialog(
        onDismissRequest = {
            if (allowDismiss) {
                onDismiss()
            }
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(text = stringResource(R.string.alert_dialog_accept))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.alert_dialog_dismiss))
            }
        },
        icon = {
            icon?.let {
                Icon(imageVector = it, contentDescription = null)
            }
        },
        title = {
            title?.let {
                Text(text = it.unwrap())
            }
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    textFieldLabel?.let {
                        Text(text = it.unwrap())
                    }
                },
                maxLines = MAX_TEMPLATE_TITLE_LINES
            )
        }
    )
}

@Preview
@Composable
private fun EditTextDialogPreview() = ICPreview {
    EditTextDialog(
        value = "value",
        onValueChange = {},
        onAccept = {},
        onDismiss = {},
        modifier = Modifier.padding(16.dp)
    )
}