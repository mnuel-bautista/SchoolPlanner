package dev.manuel.timetable.ui.screens.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.time.DayOfWeek
import java.time.LocalTime


data class ClassState(
    val id: Long = 0,
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val dayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    val timetableId: Long = 0,
    val subjectId: Long? = null,
    val subject: String? = null,
    val classroom: String? = null,
    val professorId: Long? = null,
    val professor: String? = null,
)

class ClassState2(
    var id: Long = 0,
    starts: LocalTime = LocalTime.of(8, 0),
    ends: LocalTime = LocalTime.of(9, 0),
    dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    timetableId: Long = 0,
    subjectId: Long? = null,
    subject: String? = null,
    classroom: String? = null,
    teacherId: Long? = null,
    teacher: String? = null,
) {

    var modified: Boolean = false
        private set

    val starts: MutableState<LocalTime> = mutableStateOf(starts)

    val ends: MutableState<LocalTime> = mutableStateOf(ends)

    val dayOfWeek: MutableState<DayOfWeek> = mutableStateOf(dayOfWeek)

    val timetableId: MutableState<Long> = mutableStateOf(timetableId)

    val subjectId: MutableState<Long?> = mutableStateOf(subjectId)

    val subject: MutableState<String?> = mutableStateOf(subject)

    val classroom: MutableState<String?> = mutableStateOf(classroom)

    val teacherId: MutableState<Long?> = mutableStateOf(teacherId)

    var teacher: MutableState<String?> = mutableStateOf(teacher)

    fun setTimetableId(id: Long) {
        this.timetableId.value = id
        modified = true
    }

    fun setTeacherId(id: Long) {
        this.teacherId.value = id
        modified = true
    }

    fun setTeacher(teacher: String) {
        this.teacher.value = teacher
        modified = true
    }

    fun setSubjectId(subjectId: Long) {
        this.subjectId.value = subjectId
        modified = true
    }

    fun setSubject(subject: String) {
        this.subject.value = subject
        modified = true
    }

    fun setClassroom(classroom: String) {
        this.classroom.value = classroom
        modified = true
    }

    fun setDayOfWeek(dayOfWeek: DayOfWeek) {
        this.dayOfWeek.value = dayOfWeek
        modified = true
    }

    fun setStart(start: LocalTime) {
        if (start.isAfter(ends.value)) {
            ends.value = start.plusHours(1)
        }
        starts.value = start
        modified = true
    }

    fun setEnd(end: LocalTime): Boolean {
        if (end.isBefore(starts.value)) return false

        ends.value = end
        modified = true
        return true
    }


}

