package dev.manuel.timetable.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.manuel.timetable.R

@Composable
fun TopAppBar(
    doneButtonEnabled: Boolean,
    onDone: () -> Unit,
    onNavigateUp: () -> Unit,
    onClose: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    appBarState: TopAppBarState
) {
    when (appBarState) {
        TopAppBarState.Default -> {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                title = {},
                actions = {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Checklist, contentDescription = "")
                    }

                    TextButton(
                        onClick = onDone,
                        enabled = doneButtonEnabled,
                    ) {
                        Text(text = stringResource(R.string.done).uppercase())
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
            )
        }
        TopAppBarState.Context -> {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                },
                title = {},
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Outlined.Create, contentDescription = null)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 0.dp,
            )
        }
        TopAppBarState.ContextMore -> {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                },
                title = {},
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 0.dp,
            )
        }
    }
}