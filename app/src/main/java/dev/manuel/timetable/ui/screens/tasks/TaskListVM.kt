package dev.manuel.timetable.ui.screens.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.TasksFragmentArgs
import dev.manuel.timetable.room.daos.TaskDao
import dev.manuel.timetable.room.domain.TaskState
import dev.manuel.timetable.room.domain.toState
import dev.manuel.timetable.room.domain.toTask
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TaskListScreenState(
    val tasks: List<TaskState> = emptyList(),
    val task: TaskState = TaskState(),
) {
    /**
     * List of pending tasks
     * */
    val pendingTasks: List<TaskState>
        get() = tasks.filter {
            val today = LocalDate.now()
            val isToday = it.deadline?.isEqual(today)
            val isAfter = it.deadline?.isAfter(today)

            //If deadline is null, then this task should be shown in the pending tasks tab if
            // it is not completed.
            if (it.deadline == null)
                !it.isCompleted
            else !it.isCompleted && (isAfter == true || isToday == true)
        }

    /**
     * List of completed tasks
     * */
    val completedTasks: List<TaskState>
        get() = tasks.filter { it.isCompleted }

    /**
     * List of overdue tasks
     * */
    val overdueTasks: List<TaskState>
        get() = tasks
            .filter {
                val today = LocalDate.now()
                //Deadline is before today and the task is not completed.
                it.deadline?.isBefore(today) == true && !it.isCompleted
            }

    companion object {
        val Empty = TaskListScreenState()
    }

}

@HiltViewModel
class TaskListVM @Inject constructor(
    savedState: SavedStateHandle,
    private val taskDao: TaskDao,
) : ViewModel() {

    private val taskListFlow: Flow<List<TaskState>>

    private val task: MutableStateFlow<TaskState> = MutableStateFlow(TaskState())

    private val _uiEvent: MutableSharedFlow<UIEvent> =
        MutableSharedFlow()

    val uiEvents: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()


    val editableTask: TaskState by mutableStateOf(TaskState())

    /**
     * When the TaskListScreen receives a courseId as parameter, the screen must only show the
     * tasks for an specific [Course]
     * */
    val classId: Long?

    var shouldShowSubject: Boolean by mutableStateOf(false)

    init {
        val args = TasksFragmentArgs.fromSavedStateHandle(savedState)
        classId = args.classId

        shouldShowSubject = classId == 0L

        taskListFlow =
            if (classId != 0L)
                taskDao.getAllTasksByCourseFlow(classId)
                    .map { it.toState() }
            else
                taskDao.getAllTasksFlow()
                    .map { it.toState() }
    }

    val isFloatingButtonVisible: Boolean
        get() = true


    val state = combine(taskListFlow, task) { tasks, task ->
        TaskListScreenState(tasks = tasks, task = task)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), TaskListScreenState.Empty
    )


    fun updateOrCreateTask(taskState: TaskState) {
        viewModelScope.launch {
            if (taskState.id == 0L) {
                taskDao.insert(taskState.toTask())
            } else {
                taskDao.update(taskState.toTask())
            }
        }
    }

    fun completeTask(task: TaskState) {
        viewModelScope.launch {
            taskDao.update(task.toTask().copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TaskState) {
        viewModelScope.launch {
            taskDao.delete(task.id)
        }
    }

}
