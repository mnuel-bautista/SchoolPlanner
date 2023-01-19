package dev.manuel.timetable.room.converters

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


object AppConverters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun toDayOfWeek(day: Int): DayOfWeek {
        return enumValues<DayOfWeek>()[day]
    }

    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek): Int {
        return day.ordinal
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String {
        return date?.let {  formatter.format(date) } ?: "NULL"
    }

    @TypeConverter
    fun toLocalDate(date: String): LocalDate? {
        return if(date == "NULL") null else LocalDate.parse(date)
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String {
        return time?.let {  timeFormatter.format(time) } ?: "NULL"
    }

    @TypeConverter
    fun toLocalTime(time: String): LocalTime? {
        return if(time == "NULL") null else LocalTime.parse(time)
    }


    @TypeConverter
    fun fromLocalDateTime(time: LocalDateTime?): String {
        return time?.let {  dateTimeFormatter.format(time) } ?: "NULL"
    }

    @TypeConverter
    fun toLocalDateTime(time: String): LocalDateTime? {
        return if(time == "NULL") null else LocalDateTime.parse(time)
    }
}