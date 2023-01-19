package dev.manuel.timetable.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object: Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table with "ON DELETE CASCADE" for member_chat_id foreign key action
        database.execSQL("""
            CREATE TABLE `classes_new` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `startTime` TEXT NOT NULL, 
            `endTime` TEXT NOT NULL, 
            `dayOfWeek` INTEGER NOT NULL, 
            `timetableId` INTEGER NOT NULL, 
            `subjectId` INTEGER, 
            FOREIGN KEY(`timetableId`) REFERENCES `timetables`(`id`) ON UPDATE CASCADE ON DELETE CASCADE, 
            FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE CASCADE ON DELETE CASCADE );
        """.trimIndent())

        // Copy the rows from existing table to new table
        database.execSQL("""
            INSERT INTO classes_new
            SELECT * FROM classes"""
            .trimIndent())

        // Remove the old table
        database.execSQL("DROP TABLE classes");
        // Change the new table name to the correct one
        database.execSQL("ALTER TABLE classes_new RENAME TO classes")
    }

}