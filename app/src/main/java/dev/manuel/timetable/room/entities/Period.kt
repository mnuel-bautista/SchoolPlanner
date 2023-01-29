package dev.manuel.timetable.room.entities

import androidx.room.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "periods",
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
data class Period(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val period: String = "",
    val description: String = "",
    val modifiedAt: LocalDateTime = LocalDateTime.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val classId: Long = 0,
)


data class PeriodWithGrades(
    @Embedded val period: Period = Period(),
    @Relation(
        parentColumn = "id",
        entityColumn = "periodId"
    )
    val grades: List<Grade> = emptyList()
)
