package dev.manuel.timetable.ui.screens.periods

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.R
import dev.manuel.timetable.room.daos.GradeDao
import dev.manuel.timetable.room.daos.PeriodDao
import dev.manuel.timetable.room.entities.Grade
import dev.manuel.timetable.room.entities.PeriodWithGrades
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewPeriodVM @Inject constructor(
    private val periodDao: PeriodDao,
    private val gradeDao: GradeDao,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mState: MutableStateFlow<PeriodWithGrades> =
        MutableStateFlow(PeriodWithGrades())

    val state: StateFlow<PeriodWithGrades> = mState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<UIEvent> =
        MutableSharedFlow()

    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    private var periodId = 0L

    private var classId = 0L

    init {

        val args = NewPeriodFragmentArgs.fromSavedStateHandle(savedStateHandle)
        periodId = args.periodId
        classId = args.classId

        viewModelScope.launch {
            if (periodId != 0L) {
                periodDao.getPeriodWithGradesFlow(periodId).collect {
                    mState.value = it
                }
            }
        }

    }

    fun addGrade(grade: Grade) {
        viewModelScope.launch {
            gradeDao.insert(grade.copy(periodId = periodId))

            _uiEvent.emit(UIEvent.ShowMessage(R.string.grade_added_success))
        }
    }

    fun addDescription(description: String) {
        viewModelScope.launch {
            periodDao.update(state.value.period.copy(description = description))
        }
    }


}