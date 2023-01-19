package dev.manuel.timetable.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.manuel.timetable.ui.theme.TimetableTheme

@Composable
fun TaskItem(
    title: String,
    description: String,
    deadline: String,
    reminder: String,
    isCompleted: Boolean = false,
    course: String = "",
    markAsCompleted: (Boolean) -> Unit,
    onClick: () -> Unit,
) {

    Surface {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Checkbox(
                modifier = Modifier.padding(top = 7.dp),
                checked = isCompleted,
                onCheckedChange = markAsCompleted,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = course.ifBlank { "Sin asignatura" },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    DateChip(
                        date = deadline,
                        isActive = deadline.isNotBlank(),
                        enabled = false,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TimetableTheme {
        TaskItem(
            title = "Do your math homework.",
            description = "This is a description for the text.",
            deadline = "Today",
            reminder = "12:00 PM",
            isCompleted = true,
            course = "Administración de base de datos",
            markAsCompleted = {},
            onClick = {},
        )
    }
}

@Preview
@Composable
fun TaskItemOverFlowPreview() {
    TimetableTheme {
        TaskItem(
            title = "Do your math homework. This is a very long task name",
            description = "This is a description for the text. This is a very long long long long task description. Unnecessary task description ",
            deadline = "Today",
            reminder = "12:00 PM",
            isCompleted = true,
            course = "Administración de base de datos. This is a very long course name",
            markAsCompleted = {},
            onClick = {},
        )
    }
}

@Preview
@Composable
fun TaskItemWithoutCoursePreview() {
    TimetableTheme {
        TaskItem(
            title = "Do your math homework. This is a very long task name",
            description = "This is a description for the text. This is a very long long long long task description. Unnecessary task description ",
            deadline = "Today",
            reminder = "12:00 PM",
            isCompleted = true,
            course = "",
            markAsCompleted = {},
            onClick = {},
        )
    }
}