package dev.manuel.timetable.ui.screens.timetable

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.manuel.timetable.room.entities.Timetable
import dev.manuel.timetable.ui.safeCollector
import dev.manuel.timetable.ui.theme.TimetableTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetablesScreen(
    viewModel: TimetablesVM,
    onShowDialogForTimetable: () -> Unit,
    onShowContextMenu: (Timetable) -> Unit,
) {


    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val context = LocalContext.current
    val timetables by viewModel.timetables
    val selected by viewModel.selected



    viewModel.uiEvent.safeCollector(
        onShowMessage = { message ->
            Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT)
                .show()
        },
        onShowContextualMenu = {
        },
        onHideContextualMenu = { scope.launch { sheetState.hide() } }
    )

    ScreenContent(
        timetables = timetables,
        selected = selected,
        onSelectTimetable = viewModel::selectTimetable,
        onShowDialog = onShowDialogForTimetable,
        onTimetableLongClick = onShowContextMenu,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    timetables: List<Timetable>,
    selected: Timetable?,
    onSelectTimetable: (Timetable) -> Unit,
    onShowDialog: () -> Unit,
    onTimetableLongClick: (Timetable) -> Unit,
) {


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onShowDialog) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            }
        }
    ) { _ ->
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            items(timetables) {
                TimetableItem(
                    selected = it == selected,
                    timetable = it.name,
                    onSelect = { onSelectTimetable(it) },
                    onLongClick = { onTimetableLongClick(it) },
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun TimetableItem(
    timetable: String,
    selected: Boolean = false,
    onSelect: () -> Unit,
    onLongClick: () -> Unit,
) {

    val backgroundAlpha by animateFloatAsState(
        if (selected) 0.12f else 0f
    )

    val backgroundColor = MaterialTheme.colorScheme.primary.copy(
        alpha = backgroundAlpha
    )
    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
    )

    Surface(
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier.combinedClickable(onLongClick = onLongClick) { onSelect() }
        ) {
            ListItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        tint = contentColor,
                        contentDescription = null
                    )
                },
            ) {
                Text(
                    text = timetable,
                    color = contentColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimetablesScreenPreview() {

    val timetables = listOf(
        Timetable(0, "Primer semestre"),
        Timetable(1, "Segundo semestre"),
        Timetable(2, "Tercer semestre"),
        Timetable(3, "Cuarto semestre"),
        Timetable(4, "Quinto semestre"),
        Timetable(5, "Sexto semestre"),
        Timetable(6, "Septimo semestre"),
    )

    var selected by remember { mutableStateOf(timetables[0]) }

    TimetableTheme {
        ScreenContent(
            timetables = timetables,
            selected = selected,
            onSelectTimetable = { selected = it },
            onShowDialog = {},
            onTimetableLongClick = {},
        )
    }
}
