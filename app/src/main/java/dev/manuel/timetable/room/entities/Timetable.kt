package dev.manuel.timetable.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetables")
data class Timetable(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
)