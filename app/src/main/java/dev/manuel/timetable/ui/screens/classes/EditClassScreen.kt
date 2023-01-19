package dev.manuel.timetable.ui.screens.classes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar
import dev.manuel.timetable.R
import dev.manuel.timetable.format
import dev.manuel.timetable.ui.components.SimpleTextField
import dev.manuel.timetable.ui.safeCollector
import dev.manuel.timetable.ui.theme.TimetableTheme
import java.time.DayOfWeek

private val HeaderHeight = 176.dp

@Composable
fun EditClassScreen(
    viewModel: EditClassVM,
    onAddOccurrence: () -> Unit,
    onNavigateUp: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val view = LocalView.current

    viewModel.uiEvent.safeCollector(onNavigateUp = onNavigateUp, onShowMessage = {
        Snackbar.make(view, context.getText(it), Snackbar.LENGTH_SHORT)
            .show()
    })

    CourseDetailsContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onSubjectChange = viewModel::setSubject,
        onTeacherChange = viewModel::setTeacher,
        onClassroomChange = viewModel::setClassroom,
        onAddOccurrence = onAddOccurrence,
        onDone = viewModel::saveClass,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsContent(
    uiState: EditClassState,
    onSubjectChange: (String) -> Unit,
    onTeacherChange: (String) -> Unit,
    onClassroomChange: (String) -> Unit,
    onAddOccurrence: () -> Unit,
    onDone: () -> Unit,
    onNavigateUp: () -> Unit,
) {

    val mClass = uiState.mClass

    Surface {
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Header(
                    modifier = Modifier.padding(
                        top = 8.dp,
                    ),
                    subject = mClass.subject,
                    teacher = mClass.teacher,
                    classroom = mClass.classroom,
                    onClassroomChange = onClassroomChange,
                    onSubjectChange = onSubjectChange,
                    onTeacherChange = onTeacherChange,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.padding(horizontal = 8.dp)
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        uiState.occurrences.forEach {
                            ListItem(
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                headlineText = { Text(text = it.dayOfWeek.format()) },
                                trailingContent = { Text(text = it.starts.format(it.ends)) }
                            )
                        }
                        if(uiState.occurrences.isNotEmpty()) {
                            Divider()
                        }
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape,
                            onClick = onAddOccurrence) {
                            Text("Add new occurrence")
                        }
                    }
                }
            }
            Column {
                Divider()
                TextButton(
                    shape = RectangleShape,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth(),
                    onClick = onDone
                ) {
                    Text(stringResource(id = R.string.done))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    modifier: Modifier = Modifier,
    subject: String,
    teacher: String,
    classroom: String,
    onSubjectChange: (String) -> Unit,
    onTeacherChange: (String) -> Unit,
    onClassroomChange: (String) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(HeaderHeight)
    ) {

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.School, contentDescription = null
                )
            },
            headlineText = {
                SimpleTextField(
                    value = subject,
                    onValueChange = onSubjectChange,
                    label = stringResource(id = R.string.subject)
                )
            },
        )
        Divider()
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            headlineText = {
                SimpleTextField(
                    value = teacher,
                    onValueChange = onTeacherChange,
                    label = stringResource(id = R.string.teacher)
                )
            },
        )
        Divider()
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn, contentDescription = null
                )
            },
            headlineText = {
                SimpleTextField(
                    value = classroom,
                    onValueChange = onClassroomChange,
                    label = stringResource(id = R.string.classroom)
                )
            },
        )

        Divider()
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    TimetableTheme {
        CourseDetailsContent(
            uiState = EditClassState(
                mClass = dev.manuel.timetable.room.entities.Class()
            ),
            onSubjectChange = {},
            onTeacherChange = {},
            onNavigateUp = {},
            onDone = {},
            onClassroomChange = { },
            onAddOccurrence = {}
        )
    }
}

@Preview
@Composable
private fun DarkThemePreview() {
    TimetableTheme(darkTheme = true) {
        CourseDetailsContent(
            uiState = EditClassState(
                mClass = dev.manuel.timetable.room.entities.Class()
            ),
            onSubjectChange = {},
            onTeacherChange = {},
            onNavigateUp = {},
            onDone = {},
            onClassroomChange = { },
            onAddOccurrence = {}
        )
    }
}

//Sample data for UI preview
private val courses = (0..10).map {
    ClassState(
        id = 0,
        subject = "Taller de base de datos $it",
        classroom = "LE5",
        professor = "Juan Carlos Madrigal Perez $it",
        dayOfWeek = DayOfWeek.MONDAY,
    )
}
