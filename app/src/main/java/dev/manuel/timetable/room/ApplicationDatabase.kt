package dev.manuel.timetable.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.manuel.timetable.room.converters.AppConverters
import dev.manuel.timetable.room.daos.*
import dev.manuel.timetable.room.entities.*


@Database(
    entities = [
        Timetable::class, Class::class, Task::class, Occurrence::class,
        Note::class, Period::class, Grade::class,
    ],
    views = [TaskWithSubject::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(AppConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun classDao(): ClassDao

    abstract fun taskDao(): TaskDao

    abstract fun periodDao(): PeriodDao

    abstract fun occurrenceDao(): OccurrenceDao

    abstract fun timetableDao(): TimetableDao

    abstract fun gradeDao(): GradeDao

    abstract fun noteDao(): NoteDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "timetable_database"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("timetable_database.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}