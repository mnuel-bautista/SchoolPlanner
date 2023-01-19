package dev.manuel.timetable.room.daos

import androidx.room.Dao
import androidx.room.Insert
import dev.manuel.timetable.room.entities.Occurrence

@Dao
interface OccurrenceDao {

    @Insert
    suspend fun insert(occurrence: Occurrence)

}