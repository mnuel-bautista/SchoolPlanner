package dev.manuel.timetable.room.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.manuel.timetable.room.entities.Task
import dev.manuel.timetable.room.entities.TaskWithSubject
import dev.manuel.timetable.room.entities.Class as MClass
import java.time.LocalDate
import java.time.LocalTime



class TaskState(
    id: Long = 0,
    task: String = "",
    description: String = "",
    isCompleted: Boolean = false,
    deadline: LocalDate? = null,
    reminder: LocalTime? = null,
    classId: Long? = null,
    subject: String? = null,
) {

    var id: Long by mutableStateOf(id)

    var task: String by mutableStateOf(task)

    var description: String by mutableStateOf(description)

    var isCompleted: Boolean by mutableStateOf(isCompleted)

    var deadline: LocalDate? by mutableStateOf(deadline)

    var reminder: LocalTime? by mutableStateOf(reminder)

    var subjectId: Long? by mutableStateOf(classId)

    var subject: String? by mutableStateOf(subject)

    fun setClass(mClass: MClass) {
        subjectId = mClass.id
        this.subject = mClass.subject
    }

    fun copyFrom(taskState: TaskState) {
        id = taskState.id
        task = taskState.task
        description = taskState.description
        isCompleted = taskState.isCompleted
        deadline = taskState.deadline
        reminder = taskState.reminder
        subjectId = taskState.subjectId
        subject = taskState.subject
    }

    fun clear() {
        id = 0
        task = ""
        description = ""
        isCompleted = false
        deadline = null
        reminder = null
        subjectId = null
        subject = null
    }

}

fun TaskWithSubject.toState(): TaskState {
    return TaskState(
        id,
        task,
        description,
        isCompleted,
        deadline,
        reminder,
        classId,
        subject,
    )
}

fun List<TaskWithSubject>.toState(): List<TaskState> {
    return map { it.toState() }
}

/**
 * Maps the state to a database entity.
 * */
fun TaskState.toTask(): Task {
    return Task(id, task, description, isCompleted, deadline, reminder, subjectId)
}

enum class TaskStatus {
    Pending, Completed, Overdue
}