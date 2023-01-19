package dev.manuel.timetable.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
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
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Task
import dev.manuel.timetable.ui.theme.TimetableTheme
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val NewTaskDialogHeight = 148.dp

class NewTaskDialogState(
    private val initialTask: Task = Task()
) {

    /**
     * Returns true when the user is editing an existing task, false otherwise.
     * Tasks with a 0 identifier are new tasks.
     * */
    val isEditing: Boolean
        get() {
            return initialTask.id != 0L
        }

    var title: String by mutableStateOf(initialTask.task)

    var description: String by mutableStateOf(initialTask.description)

    var deadline: LocalDate? by mutableStateOf(initialTask.deadline)

    var reminder: LocalTime? by mutableStateOf(initialTask.reminder)

    val formattedDeadline: String
        get() {
            return deadline?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                ?: "No due date"
        }

    /**
     * Maps this state to a [Task] object.
     * */
    fun asTask(): Task {
        return initialTask.copy(
            task = title,
            description = description,
            deadline = deadline,
            reminder = reminder
        )
    }

}

/**
 * @param onDone When the user has finished editing the [Task].
 * */
@Composable
fun NewTaskDialog(
    state: NewTaskDialogState,
    onDeleteTask: () -> Unit,
    onDone: () -> Unit,
) {

    val context = LocalContext.current
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

    Surface(elevation = 4.dp) {
        Column(
            modifier = Modifier
                .height(NewTaskDialogHeight)
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = state.title,
                    onValueChange = { state.title = it },
                    textStyle = MaterialTheme.typography.subtitle1
                        .copy(color = LocalContentColor.current.copy(alpha = ContentAlpha.high)),
                    cursorBrush = SolidColor(LocalContentColor.current)
                ) { innerTextField ->
                    if (state.title.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.task_name),
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
                    innerTextField()
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = state.description,
                    onValueChange = { state.description = it },
                    textStyle = MaterialTheme.typography.body1
                        .copy(color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)),
                    cursorBrush = SolidColor(LocalContentColor.current)
                ) { innerTextField ->
                    if (state.description.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.task_description),
                            style = MaterialTheme.typography.subtitle1,
                            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        )
                    }
                    innerTextField()
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                DateChip(
                    date = state.formattedDeadline,
                    isActive = state.deadline != null,
                    onClick = {
                        showDatePicker(fragmentManager, onDateSet = { state.deadline = it })
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*IconButton(
                        onClick = {
                            showTimePicker(
                                fragmentManager,
                                title = "Select time",
                                time = state.reminder ?: LocalTime.now(),
                                onSetTime = { state.reminder = it }
                            )
                        },
                        enabled = state.deadline != null,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationAdd,
                            contentDescription = null
                        )
                    }*/
                    IconButton(onClick = onDeleteTask) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                    IconButton(onClick = onDone) {
                        Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
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
        .setTitleText("Select date")
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

@Preview
@Composable
private fun NewTaskDialogPreview() {
    TimetableTheme {
        NewTaskDialog(NewTaskDialogState(),
            onDeleteTask = {},
            onDone = {})
    }
}