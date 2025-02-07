package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.MAX_TEMPLATE_TITLE_LINES
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun RenameTemplateDialog(
    value: String,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = true
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
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(stringResource(R.string.rename_template_dialog_input_label))
                },
                maxLines = MAX_TEMPLATE_TITLE_LINES
            )
        }
    )
}


@Preview
@Composable
private fun RenameTemplateDialogPreview() = ICPreview {
    RenameTemplateDialog(
        value = "New name",
        onAccept = {},
        onDismiss = {},
        onValueChange = {},
        modifier = Modifier.padding(16.dp)
    )
}