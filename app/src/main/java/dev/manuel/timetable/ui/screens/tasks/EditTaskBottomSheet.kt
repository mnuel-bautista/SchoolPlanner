package dev.manuel.timetable.ui.screens.tasks

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.internal.managers.ViewComponentManager
import dev.manuel.timetable.R
import dev.manuel.timetable.format
import dev.manuel.timetable.room.domain.TaskState
import dev.manuel.timetable.ui.components.DateChip
import dev.manuel.timetable.ui.theme.TimetableTheme
import java.time.*


private val NewTaskDialogHeight = 148.dp

@Composable
fun EditTaskBottomSheet(
    state: TaskState,
    showSubject: Boolean = false,
    onSelectSubject: () -> Unit,
    onDeleteTask: () -> Unit,
    onUpdateOrCreateTask: (TaskState) -> Unit,
) {

    val mContext = LocalContext.current
    val context = if (mContext is ViewComponentManager.FragmentContextWrapper) {
        mContext.baseContext
    } else mContext
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

    var confirmationDialog by remember { mutableStateOf(false) }

    if (confirmationDialog) {
        ConfirmMessageDialog(
            onConfirm = {
                confirmationDialog = false
                onDeleteTask()
            },
            onDismiss = { confirmationDialog = false }
        )
    }

    Surface(shadowElevation = 4.dp) {
        Column(
            modifier = Modifier
                .heightIn(min = NewTaskDialogHeight)
                .fillMaxWidth()
        ) {
            if (showSubject) {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onSelectSubject() }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.subject ?: "Sin asignatura",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.height(4.dp))
            }

            val topPadding = if (showSubject) 0.dp else 16.dp

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = topPadding)
            ) {
                Box(contentAlignment = Alignment.CenterStart) {
                    BasicTextField(
                        value = state.task,
                        onValueChange = { state.task = it },
                        textStyle = MaterialTheme.typography.titleMedium
                            .copy(color = LocalContentColor.current.copy(alpha = ContentAlpha.high)),
                        cursorBrush = SolidColor(LocalContentColor.current),
                        maxLines = 1,
                    ) { innerTextField ->
                        if (state.task.isBlank()) {
                            Text(
                                text = stringResource(id = R.string.task_name),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        innerTextField()
                    }
                }

                Spacer(modifier = Modifier.heightIn(4.dp))

                Box(contentAlignment = Alignment.CenterStart) {
                    BasicTextField(
                        value = state.description,
                        onValueChange = { state.description = it },
                        textStyle = MaterialTheme.typography.bodyLarge
                            .copy(color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)),
                        cursorBrush = SolidColor(LocalContentColor.current),
                        maxLines = 2
                    ) { innerTextField ->
                        if (state.description.isBlank()) {
                            Text(
                                text = stringResource(id = R.string.task_description),
                                style = MaterialTheme.typography.titleMedium,
                                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                            )
                        }
                        innerTextField()
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    DateChip(
                        date = state.deadline.format(context),
                        isActive = state.deadline != null,
                        onClick = {
                            showDatePicker(
                                context,
                                fragmentManager,
                                onDateSet = { state.deadline = it })
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { confirmationDialog = true }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                        }
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            IconButton(onClick = { onUpdateOrCreateTask(state) }) {
                                Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun showTimePicker(
    fragmentManager: FragmentManager,
    title: String,
    time: LocalTime,
    onSetTime: (LocalTime) -> Unit
) {
    val picker =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(time.hour)
            .setMinute(time.minute)
            .setTitleText(title)
            .build()

    picker.addOnPositiveButtonClickListener { onSetTime(LocalTime.of(picker.hour, picker.minute)) }
    picker.show(fragmentManager, "")
}

private fun showDatePicker(
    context: Context,
    fragmentManager: FragmentManager,
    onDateSet: (LocalDate) -> Unit,
) {

    val today = LocalDateTime.now()
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val constraints = CalendarConstraints.Builder()
        .setStart(today)
        .build()

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(context.getString(R.string.select_date))
        .setCalendarConstraints(constraints)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    datePicker.addOnPositiveButtonClickListener {
        datePicker.selection?.let { selection ->
            val date = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            onDateSet(date)
        }
    }
    datePicker.show(fragmentManager, "")
}

@Composable
private fun ConfirmMessageDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.confirm_delete_task)) },
        text = { Text(text = stringResource(id = R.string.confirm_delete_task_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.delete).uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel).uppercase())
            }
        }
    )
}

@Preview
@Composable
private fun NewTaskDialogPreview() {
    TimetableTheme {}
}