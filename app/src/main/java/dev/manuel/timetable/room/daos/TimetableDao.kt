package dev.manuel.timetable.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.manuel.timetable.room.entities.Timetable
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    @Insert
    suspend fun insert(timetable: Timetable): Long

    @Update
    suspend fun update(timetable: Timetable)

    @Query("DELETE FROM timetables WHERE id =:id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM timetables")
    fun getAllTimetables(): Flow<List<Timetable>>

    @Query("SELECT * FROM timetables WHERE id = :timetableId")
    suspend fun getTimetable(timetableId: Long): Timetable


}