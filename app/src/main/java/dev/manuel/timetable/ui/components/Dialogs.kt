package dev.manuel.timetable.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.manuel.timetable.R

/**
 * Dialog visible when the user tries to navigate back and the course name is empty.
 * */
@Composable
fun DiscardChangesDialog(
    onCancelDiscardChanges: () -> Unit,
    onDiscardChanges: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelDiscardChanges,
        title = { androidx.compose.material3.Text(text = stringResource(id = R.string.discard_title)) },
        text = { androidx.compose.material3.Text(text = stringResource(id = R.string.discard_message)) },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onCancelDiscardChanges) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDiscardChanges) {
                Text(text = stringResource(id = R.string.discard))
            }
        }
    )
}


/**
 * Component used for confirming the deletion of some information.
 * */
@Composable
fun DeleteMessageDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text(text = title) },
        text = { androidx.compose.material3.Text(text = message) },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onConfirm) {
                androidx.compose.material3.Text(text = stringResource(id = R.string.delete).uppercase())
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                androidx.compose.material3.Text(text = stringResource(id = R.string.cancel).uppercase())
            }
        }
    )
}