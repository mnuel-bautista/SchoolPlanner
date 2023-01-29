package dev.manuel.timetable.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "grades",
    foreignKeys = [
        ForeignKey(
            entity = Period::class,
            parentColumns = ["id"],
            childColumns = ["periodId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val grade: Int = 0,
    val task: String = "",
    val weight: Int = 0,
    val periodId: Long = 0,
)
