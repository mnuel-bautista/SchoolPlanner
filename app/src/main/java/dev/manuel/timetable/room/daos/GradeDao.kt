package dev.manuel.timetable.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.manuel.timetable.room.entities.Grade
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @Insert
    suspend fun insert(grade: Grade): Long

    @Insert
    suspend fun update(grade: Grade)

    @Query("SELECT * FROM grades WHERE id =:periodId")
    fun getGradesByPeriod(periodId: Long): Flow<List<Grade>>

}