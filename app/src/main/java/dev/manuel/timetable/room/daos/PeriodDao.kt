package dev.manuel.timetable.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.manuel.timetable.room.entities.Period
import dev.manuel.timetable.room.entities.PeriodWithGrades
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {

    @Insert
    suspend fun insert(period: Period): Long

    @Update
    suspend fun update(period: Period)

    @Query("SELECT * FROM periods WHERE classId =:classId")
    fun getAllPeriodsByClass(classId: Long): Flow<List<Period>>

    @Query("SELECT * FROM periods WHERE id =:periodId")
    suspend fun getPeriodWithGrades(periodId: Long): PeriodWithGrades

    @Query("SELECT * FROM periods WHERE id =:periodId")
    fun getPeriodWithGradesFlow(periodId: Long): Flow<PeriodWithGrades>

}