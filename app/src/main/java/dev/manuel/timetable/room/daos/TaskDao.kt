package dev.manuel.timetable.room.daos

import androidx.room.*
import dev.manuel.timetable.room.entities.Task
import dev.manuel.timetable.room.entities.TaskWithSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks WHERE id =:taskId")
    suspend fun delete(taskId: Long)

    @Query("SELECT * FROM tasks WHERE id =:taskId")
    suspend fun getTaskById(taskId: Long): Task

    @Query("SELECT * FROM tasks_with_class WHERE id =:taskId")
    suspend fun getTaskWithSubject(taskId: Long): TaskWithSubject

    @Query("SELECT * FROM tasks_with_class")
    fun getAllTasksFlow(): Flow<List<TaskWithSubject>>

    @Query("SELECT * FROM tasks_with_class WHERE classId =:classId")
    fun getAllTasksByCourseFlow(classId: Long): Flow<List<TaskWithSubject>>

}