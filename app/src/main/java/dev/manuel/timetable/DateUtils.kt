package dev.manuel.timetable

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

fun LocalTime?.format(): String {
    return this?.let { timeFormatter.format(this) } ?: ""
}

fun LocalTime?.format(endTime: LocalTime): String {
    return this?.let {
        "${timeFormatter.format(this)} - ${timeFormatter.format(endTime)}"
    } ?: ""
}

fun LocalDate?.format(context: Context): String {
    //TODO Localize the default value if LocalDate is null.
    return this?.let { dateFormatter.format(this) } ?: context.getString(R.string.no_date)
}

fun DayOfWeek.format(): String {
    return this.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
}