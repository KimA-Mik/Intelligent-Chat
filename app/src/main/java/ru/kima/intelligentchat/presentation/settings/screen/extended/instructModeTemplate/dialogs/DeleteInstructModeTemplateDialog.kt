package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun DeleteInstructModeTemplateDialog(
    templateName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    SimpleAlertDialog(
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        title = stringResource(R.string.alert_dialog_title_delete_instruct_mode_template),
        text = stringResource(
            R.string.alert_dialog_text_delete_instruct_mode_template,
            templateName
        ),
        icon = Icons.Default.DeleteForever,
        modifier = modifier,
        confirmText = stringResource(R.string.action_delete),
        dismissText = stringResource(R.string.action_cancel)
    )
}

@Preview
@Composable
private fun DeleteInstructModeTemplateDialogPreview() = ICPreview {
    DeleteInstructModeTemplateDialog(
        templateName = "Template",
        onConfirm = {},
        onDismiss = {},
        modifier = Modifier.padding(16.dp)
    )
}