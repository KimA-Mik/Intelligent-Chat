package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.presentation.settings.util.composeSting
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun IncludeNamePolicySelectDialog(
    selectedPolicy: IncludeNamePolicy,
    onAccept: (IncludeNamePolicy) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.include_string_policy_setting_title))
        },
        text = {
            IncludeNamePolicySelector(
                selectedPolicy = selectedPolicy,
                selectPolicy = onAccept
            )
        }
    )
}

@Composable
fun IncludeNamePolicySelector(
    selectedPolicy: IncludeNamePolicy,
    selectPolicy: (IncludeNamePolicy) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(IncludeNamePolicy.entries) {
            val name = it.composeSting().unwrap()
            ListItem(
                headlineContent = {
                    Text(text = name)
                },
                modifier = Modifier.clickable { selectPolicy(it) },
                leadingContent = {
                    Icon(
                        imageVector = if (it == selectedPolicy) Icons.Default.RadioButtonChecked
                        else Icons.Default.RadioButtonUnchecked,
                        contentDescription = name
                    )
                },
                colors = ListItemDefaults.colors(containerColor = AlertDialogDefaults.containerColor)
            )
        }
    }
}

@Preview
@Composable
private fun IncludeNamePolicySelectDialogPreview() = ICPreview {
    IncludeNamePolicySelectDialog(
        selectedPolicy = IncludeNamePolicy.ALWAYS,
        onAccept = { },
        onDismiss = { },
        modifier = Modifier.padding(16.dp)
    )
}