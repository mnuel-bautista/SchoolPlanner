package dev.manuel.timetable.room.daos

import androidx.room.*
import dev.manuel.timetable.room.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Insert
    suspend fun insert(course: Class): Long

    @Insert
    suspend fun insertOccurrence(occurrence: Occurrence): Long

    @Transaction
    suspend fun insertClassWithOccurrences(mClass: Class, occurrences: List<Occurrence>) {
        val classId = insert(mClass)
        occurrences.map { it.copy(classId = classId) }
            .forEach { insertOccurrence(it) }
    }

    @Update
    suspend fun update(course: Class)

    @Query("DELETE FROM classes WHERE id =:courseId")
    suspend fun delete(courseId: Long)

    @Query("SELECT * FROM classes WHERE id = :classId")
    suspend fun getClassById(classId: Long): Class

    @Transaction
    @Query("SELECT * FROM classes WHERE id = :classId")
    fun getClassWithOccurrences(classId: Long): Flow<ClassWithOccurrences>

    @Transaction
    @Query("SELECT * FROM classes")
    fun getOccurrencesWithClass(): Flow<List<OccurrenceWithClass>>

    @Query("SELECT * FROM classes WHERE id = :courseId")
    fun getCourseByIdFlow(courseId: Long): Flow<Class>

    @Query("SELECT * FROM classes WHERE id =:classId")
    fun getClassByIdFlow(classId: Long): Flow<Class>

    @Query("SELECT * FROM classes WHERE timetableId =:timetableId")
    fun getAllClassesByTimetableFlow(timetableId: Long): Flow<List<Class>>

    @Query("SELECT * FROM classes")
    fun getAllClassesFlow(): Flow<List<Class>>

}