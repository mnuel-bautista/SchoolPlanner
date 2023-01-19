package dev.manuel.timetable.ui.screens.tasks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.*
import dev.manuel.timetable.format
import dev.manuel.timetable.room.domain.TaskState
import dev.manuel.timetable.room.domain.TaskStatus
import dev.manuel.timetable.ui.components.TaskItem
import dev.manuel.timetable.ui.theme.TimetableTheme
import kotlinx.coroutines.launch
import dev.manuel.timetable.R
import java.lang.IllegalStateException


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListVM,
    onNavigateToSelectSubject: () -> Unit,
    navigateUp: () -> Unit,
) {

    val uiState by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            EditTaskBottomSheet(
                state = viewModel.editableTask,
                showSubject = viewModel.shouldShowSubject,
                onSelectSubject = onNavigateToSelectSubject,
                onDeleteTask = {
                    viewModel.deleteTask(viewModel.editableTask)
                    scope.launch { sheetState.hide() }
                },
                onUpdateOrCreateTask = {
                    viewModel.updateOrCreateTask(viewModel.editableTask)
                    scope.launch { sheetState.hide() }
                    keyboardController?.hide()
                },
            )
        }
    ) {
        TaskListContent(
            uiState = uiState,
            pagerState = pagerState,
            isFloatingButtonVisible = viewModel.isFloatingButtonVisible,
            navigateUp = navigateUp,
            onCreateNewTask = {
                viewModel.editableTask.clear()

                if (!viewModel.shouldShowSubject) {
                    viewModel.editableTask.subjectId = viewModel.classId
                }

                scope.launch { sheetState.show() }
            },
            taskList = {
                TaskListPager(
                    state = uiState,
                    pagerState = pagerState,
                    onTaskClick = {
                        viewModel.editableTask.copyFrom(it)
                        scope.launch { sheetState.show() }
                    },
                    onCompleteTask = viewModel::completeTask,
                )
            },
        )
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskListContent(
    uiState: TaskListScreenState,
    pagerState: PagerState,
    isFloatingButtonVisible: Boolean = false,
    onCreateNewTask: () -> Unit = {},
    taskList: @Composable () -> Unit,
    navigateUp: () -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            if (isFloatingButtonVisible) {
                androidx.compose.material3.ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(id = R.string.new_task)) },
                    onClick = { onCreateNewTask() },
                    icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = "") },
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    ) {
        Column {

            val coroutineScope = rememberCoroutineScope()

            androidx.compose.material.TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colorScheme.background,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {

                TaskStatusRows(
                    status = TaskStatus.values()[pagerState.currentPage],
                    onStatusChange = {
                        coroutineScope.launch { pagerState.animateScrollToPage(it.ordinal) }
                    }
                )

            }

            taskList()
        }
    }

}

@Composable
private fun TaskStatusRows(
    status: TaskStatus,
    onStatusChange: (TaskStatus) -> Unit,
) {

    TaskStatus.values().forEach {

        val taskStatus = when (it.ordinal) {
            0 -> stringResource(id = R.string.pending)
            1 -> stringResource(id = R.string.completed)
            2 -> stringResource(id = R.string.overdue)
            else -> throw IllegalStateException("Wrong task status")
        }

        Tab(
            selected = status == it, onClick = { onStatusChange(it) },
            text = { Text(text = taskStatus) },
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TaskListPager(
    state: TaskListScreenState,
    pagerState: PagerState,
    onTaskClick: ((task: TaskState) -> Unit)? = null,
    onCompleteTask: (TaskState) -> Unit,
) {
    HorizontalPager(count = 3, state = pagerState) { page ->
        when (page) {
            0 -> TaskList(
                tasks = state.pendingTasks,
                onTaskClick = { onTaskClick?.invoke(it) },
                onCompleteTask = onCompleteTask
            )
            1 -> TaskList(
                tasks = state.completedTasks,
                onTaskClick = { onTaskClick?.invoke(it) },
                onCompleteTask = onCompleteTask
            )
            2 -> TaskList(
                tasks = state.overdueTasks,
                onTaskClick = { onTaskClick?.invoke(it) },
                onCompleteTask = onCompleteTask
            )
        }
    }
}

@Composable
fun TaskList(
    tasks: List<TaskState>,
    onTaskClick: (task: TaskState) -> Unit,
    onCompleteTask: (TaskState) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(tasks, key = { it.id }) { task ->
            TaskItem(
                title = task.task,
                isCompleted = task.isCompleted,
                description = task.description,
                deadline = task.deadline.format(context),
                reminder = "",
                course = task.subject ?: "",
                markAsCompleted = { onCompleteTask(task) },
                onClick = { onTaskClick(task) }
            )
            Divider()
        }
    }
}

@Preview
@Composable
private fun TaskListScreenPreview() {
    TimetableTheme {

    }
}