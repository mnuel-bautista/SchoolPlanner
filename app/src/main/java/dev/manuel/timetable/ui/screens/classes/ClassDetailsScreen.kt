package dev.manuel.timetable.ui.screens.classes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import dev.manuel.timetable.R
import dev.manuel.timetable.format
import dev.manuel.timetable.ui.safeCollector

@Composable
fun ClassDetailsScreen(
    viewModel: ClassDetailsVM,
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
        onDeleteClass = viewModel::onDeleteClass,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassDetailsContent(
    uiState: ClassDetailsScreenState,
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
                leadingContent = { Icon(imageVector = Icons.Outlined.School, contentDescription = null) },
                headlineText = { Text(text = stringResource(id = R.string.subject)) },
                supportingText = { Text(text = mClass.subject) }
            )

            ListItem(
                leadingContent = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) },
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
                leadingContent = { Icon(imageVector = Icons.Outlined.Schedule, contentDescription = null) },
                headlineText = { Text(text = stringResource(id = R.string.timetable)) },
                supportingText = { Text(text = timetable) }
            )

            if(occurrences.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(occurrences) {
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
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.no_classes))
                }
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
    ClassDetailsContent(
        ClassDetailsScreenState(),
        onEditClass = {},
        onDeleteClass = {},
        onNavigateUp = {}
    )
}