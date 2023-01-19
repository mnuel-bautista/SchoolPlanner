package dev.manuel.timetable.room.entities

import androidx.room.*
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Class::class,
            parentColumns = ["id"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val task: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    @ColumnInfo(defaultValue = "NULL") val deadline: LocalDate? = null,
    @ColumnInfo(defaultValue = "NULL") val reminder: LocalTime? = null,
    val classId: Long? = null,
)

@DatabaseView(
    """
        SELECT t.id, t.task, t.description, t.isCompleted, t.deadline, 
            t.reminder, t.classId, s.subject
        FROM tasks t LEFT JOIN classes s ON t.classId = s.id
    """,
    viewName = "tasks_with_class"
)
data class TaskWithSubject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val task: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    @ColumnInfo(defaultValue = "NULL") val deadline: LocalDate? = null,
    @ColumnInfo(defaultValue = "NULL") val reminder: LocalTime? = null,
    val classId: Long? = null,
    val subject: String? = null,
)