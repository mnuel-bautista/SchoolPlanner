package dev.manuel.timetable.room.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = "notes",
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
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isFavorite: Boolean = false,
    val isPinned: Boolean = false,
    val modifiedAt: LocalDateTime = LocalDateTime.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val classId: Long? = null
)

data class NoteWithClass(
    @Embedded val mClass: Class = Class(),
    @Relation(
        parentColumn = "id",
        entityColumn = "classId"
    )
    val note: Note = Note()
)