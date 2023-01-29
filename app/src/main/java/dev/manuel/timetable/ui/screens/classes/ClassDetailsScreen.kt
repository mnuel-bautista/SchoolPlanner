package dev.manuel.timetable.ui.screens.classes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dev.manuel.timetable.R
import dev.manuel.timetable.format
import dev.manuel.timetable.room.entities.Period
import dev.manuel.timetable.ui.safeCollector

@Composable
fun ClassDetailsScreen(
    viewModel: ClassDetailsVM,
    onCreatePeriod: () -> Unit,
    onNavigateToPeriod: (Period) -> Unit,
    onNavigateToEditClass: () -> Unit,
    onNavigateUp: () -> Unit,
) {

    val uiState by viewModel.state.collectAsState()

    if (viewModel.confirmDeletion) {
        DeleteClassDialog(
            onCancelDeletion = viewModel::cancelClassDeletion,
            onConfirmDeletion = viewModel::confirmClassDeletion,
        )
    }

    viewModel.uiEvent.safeCollector(
        onNavigateUp = onNavigateUp
    )

    ClassDetailsContent(
        uiState = uiState,
        onEditClass = onNavigateToEditClass,
        onCreatePeriod = onCreatePeriod,
        onNavigateToPeriod = onNavigateToPeriod,
        onDeleteClass = viewModel::onDeleteClass,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassDetailsContent(
    uiState: ClassDetailsScreenState,
    onCreatePeriod: () -> Unit,
    onNavigateToPeriod: (Period) -> Unit,
    onEditClass: () -> Unit,
    onDeleteClass: () -> Unit,
    onNavigateUp: () -> Unit,
) {

    val mClass = uiState.classWithOccurrences.mClass
    val occurrences = uiState.classWithOccurrences.occurrences
    val timetable = uiState.timetable.name

    Surface {
        Column {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.School,
                        contentDescription = null
                    )
                },
                headlineText = { Text(text = stringResource(id = R.string.subject)) },
                supportingText = { Text(text = mClass.subject) }
            )

            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                },
                headlineText = { Text(text = stringResource(id = R.string.teacher)) },
                supportingText = { Text(text = mClass.teacher) }
            )

            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.LocationCity,
                        contentDescription = null
                    )
                },
                headlineText = { Text(text = stringResource(id = R.string.classroom)) },
                supportingText = { Text(text = mClass.classroom) }
            )

            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null
                    )
                },
                headlineText = { Text(text = stringResource(id = R.string.timetable)) },
                supportingText = { Text(text = timetable) }
            )

            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = "Occurrences",
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if(occurrences.isNotEmpty()) {
                    occurrences.forEach {
                        Card {
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ),
                                overlineText = { Text(text = it.dayOfWeek.format()) },
                                headlineText = { Text(text = mClass.subject) },
                                supportingText = { Text(text = it.starts.format(it.ends)) }
                            )
                        }
                    }
                } else {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.no_classes))
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Text("Create New Occurrence")
                }
            }

            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = "Periods",
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if(uiState.periods.isNotEmpty()) {
                    uiState.periods.forEach {
                        OutlinedCard(colors = CardDefaults.outlinedCardColors(), onClick = {
                            onNavigateToPeriod(it)
                        }) {
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ),
                                headlineText = { Text(text = it.period) },
                                supportingText = { Text(text = it.description) }
                            )
                        }
                    }
                } else {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "This class doesn't have any periods yet")
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { onCreatePeriod() }) {
                    Text("Create New Period")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DeleteClassDialog(
    onCancelDeletion: () -> Unit,
    onConfirmDeletion: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelDeletion,
        title = { Text(text = stringResource(id = R.string.confirm_delete_class)) },
        text = { Text(text = stringResource(id = R.string.confirm_delete_class_message)) },
        dismissButton = {
            TextButton(onClick = onCancelDeletion) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmDeletion) {
                Text(text = stringResource(id = R.string.delete))
            }
        }
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    Mdc3Theme {
        ClassDetailsContent(
            ClassDetailsScreenState(
                periods = listOf(
                    Period(1, "First Semester", "Lorem Ipsum"),
                    Period(2, "Second Semester", "Lorem Ipsum"),
                    Period(3, "Third Semester", "Lorem Ipsum"),
                )
            ),
            onCreatePeriod = {},
            onNavigateToPeriod = {},
            onEditClass = {},
            onDeleteClass = {},
            onNavigateUp = {}
        )
    }
}