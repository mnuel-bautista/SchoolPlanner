package dev.manuel.timetable.room.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(
    tableName = "classes",
    foreignKeys = [
        ForeignKey(
            entity = Timetable::class,
            parentColumns = ["id"],
            childColumns = ["timetableId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
@Parcelize
data class Class(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timetableId: Long = 0,
    val classroom: String = "",
    val subject: String = "",
    val teacher: String = "",
) : Parcelable

@Entity(
    tableName = "occurrences",
    foreignKeys = [
        ForeignKey(
            entity = Class::class,
            parentColumns = ["id"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Occurrence(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val starts: LocalTime = LocalTime.of(LocalTime.now().hour, 0),
    val ends: LocalTime = LocalTime.of(LocalTime.now().hour, 0).plusHours(1),
    val classId: Long = 0,
)


data class ClassWithOccurrences(
    @Embedded val mClass: Class = Class(),
    @Relation(
        parentColumn = "id",
        entityColumn = "classId"
    )
    val occurrences: List<Occurrence> = emptyList()
)

data class OccurrenceWithClass(
    @Embedded val mClass: Class = Class(),
    @Relation(
        parentColumn = "id",
        entityColumn = "classId"
    )
    val occurrence: Occurrence
)


