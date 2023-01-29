package dev.manuel.timetable.di

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.manuel.timetable.SHARED_PREFERENCES_TAG
import dev.manuel.timetable.room.ApplicationDatabase
import dev.manuel.timetable.room.daos.*

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun providesPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)
    }

    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        return ApplicationDatabase.getDatabase(context)
    }

    @Provides
    fun providesAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    fun providesClassDao(db: ApplicationDatabase) = db.classDao()
    @Provides
    fun providesOccurrenceDao(db: ApplicationDatabase) = db.occurrenceDao()

    @Provides
    fun providesTimetableDao(db: ApplicationDatabase) = db.timetableDao()

    @Provides
    fun providesTaskDao(db: ApplicationDatabase) = db.taskDao()

    @Provides
    fun providesNoteDao(db: ApplicationDatabase) = db.noteDao()
    @Provides
    fun providesPeriodDao(db: ApplicationDatabase): PeriodDao = db.periodDao()
    @Provides
    fun providesGradeDao(db: ApplicationDatabase) = db.gradeDao()

}