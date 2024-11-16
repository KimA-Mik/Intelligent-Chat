package ru.kima.intelligentchat.presentation.common.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun runSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    onActionPerformed: () -> Unit,
    onActionDismissed: (() -> Unit)? = null,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Long,
    withDismissAction: Boolean = true
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = withDismissAction
    )

    when (result) {
        SnackbarResult.Dismissed -> onActionDismissed?.invoke()
        SnackbarResult.ActionPerformed -> onActionPerformed()
    }
}
