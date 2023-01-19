package dev.manuel.timetable.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.manuel.timetable.room.entities.Note
import dev.manuel.timetable.room.entities.NoteWithClass
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): Note

    @Query("SELECT * FROM classes")
    fun getNotesWithClass(): Flow<List<NoteWithClass>>

    @Query("SELECT * FROM classes WHERE id = :noteId")
    suspend fun getNoteWithClassById(noteId: Long): NoteWithClass

    @Query("SELECT * FROM notes")
    fun getAllNotesFlow(): Flow<List<Note>>

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun delete(noteId: Long)



}