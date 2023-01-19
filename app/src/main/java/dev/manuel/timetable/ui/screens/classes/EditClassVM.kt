package dev.manuel.timetable.ui.screens.classes

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.NewClassFragmentArgs
import dev.manuel.timetable.R
import dev.manuel.timetable.SELECTED_TIMETABLE_ID
import dev.manuel.timetable.room.daos.ClassDao
import dev.manuel.timetable.room.entities.Class as MClass
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.manuel.timetable.room.entities.Occurrence

data class EditClassState(
    val mClass: MClass = MClass(),
    val occurrences: List<Occurrence> = emptyList()
) {
    val modified = false
}

@HiltViewModel
class EditClassVM @Inject constructor(
    private val classDao: ClassDao,
    savedStateHandle: SavedStateHandle,
    preferences: SharedPreferences,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState: MutableStateFlow<EditClassState> = MutableStateFlow(EditClassState())

    val uiState: StateFlow<EditClassState> = _uiState.asStateFlow()

    private val mClass: MutableStateFlow<MClass> = MutableStateFlow(MClass())

    private val occurrences: MutableStateFlow<List<Occurrence>> =
        MutableStateFlow(listOf())

    private val id: MutableStateFlow<Long> = MutableStateFlow(0)


    /**
     * The identifier for the timetable this course belongs to.
     * */
    private var timetableId: Long = 0L

    /**
     * It is used for updating the state for the day of the week. When this screen is used for creating
     * a new course, then the value passed as argument will be the initial value for the day of the week.
     * */
    private var dayOfWeek: Int? = 0

    init {

        val args = NewClassFragmentArgs.fromSavedStateHandle(savedStateHandle)

        id.value = args.classId
        dayOfWeek = args.dayOfWeek

        timetableId = preferences.getLong(SELECTED_TIMETABLE_ID, 0L)

        viewModelScope.launch {
            combine(mClass, occurrences) { mClass, occurrences ->
                EditClassState(mClass, occurrences)
            }.collect { _uiState.value = it }
        }
    }


    fun setSubject(subject: String) {
        mClass.value = mClass.value.copy(subject = subject)
    }

    fun setTeacher(teacher: String) {
        mClass.value = mClass.value.copy(teacher = teacher)
    }

    fun setClassroom(classroom: String) {
        mClass.value = mClass.value.copy(classroom = classroom)
    }

    fun addOccurrence(occurrence: Occurrence) {
        val occurrences = uiState.value.occurrences
        this.occurrences.value = occurrences + occurrence
    }

    fun saveClass() {
        viewModelScope.launch {

            if(timetableId == 0L) {
                _uiEvent.emit(UIEvent.ShowMessage(R.string.select_timetable))
                return@launch
            }

            val value = mClass.value

            if(value.subject.isBlank()) {
                _uiEvent.emit(UIEvent.ShowMessage(R.string.empty_subject))
                return@launch
            }

            if(value.teacher.isBlank()) {
                _uiEvent.emit(UIEvent.ShowMessage(R.string.empty_teacher))
                return@launch
            }

            if(value.classroom.isBlank()) {
                _uiEvent.emit(UIEvent.ShowMessage(R.string.empty_classroom))
                return@launch
            }

            val newClass = mClass.value.copy(timetableId = timetableId)
            classDao.insertClassWithOccurrences(newClass, occurrences.value)

            _uiEvent.emit(UIEvent.ShowMessage(R.string.class_success))
            _uiEvent.emit(UIEvent.NavigateBack)
        }
    }

}