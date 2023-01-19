package dev.manuel.timetable.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.manuel.timetable.R


@Composable
fun NewTimetableDialog(
    timetable: String,
    onAccept: (timetable: String) -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(timetable) }

    NewTimetableDialogContent(
        value = name,
        onValueChange = { name = it },
        onCancel = onCancel
    ) { onAccept(name) }

}

@Composable
fun NewProfessorDialog(
    professor: String,
    onProfessorChange: (professor: String) -> Unit,
    onCancel: () -> Unit,
    onAccept: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        DialogContent(
            value = professor,
            onValueChange = onProfessorChange,
            title = stringResource(id = R.string.new_professor),
            placeholder = stringResource(id = R.string.professor),
            onCancel = onCancel,
            onAccept = onAccept
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogContent(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    placeholder: String,
    onCancel: () -> Unit,
    onAccept: () -> Unit,
) {

    Surface {
        Column(
            modifier = Modifier
                .width(280.dp)
                .height(168.dp)
                .padding(start = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
            )
            TextField(
                modifier = Modifier.padding(end = 16.dp),
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(R.string.cancel).uppercase())
                }
                TextButton(onClick = onAccept) {
                    Text(text = stringResource(R.string.accept).uppercase())
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun NewTimetableDialogContent(
    value: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {

            Text(
                text = stringResource(id = R.string.new_timetable),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value, onValueChange = onValueChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth()) {
                OutlinedButton(modifier = Modifier.weight(1f), onClick = {
                    keyboardController?.hide()
                    onCancel()
                }) {
                    Text(text = stringResource(id = R.string.cancel).uppercase())
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(modifier = Modifier.weight(1f), onClick = {
                    keyboardController?.hide()
                    onSave()
                }) {
                    Text(text = stringResource(id = R.string.accept).uppercase())
                }
            }
        }
    }
}

@Preview
@Composable
fun NewTimetableDialgoPreview() {
    NewTimetableDialogContent(value = "", { }, onCancel = {}) {  }
}