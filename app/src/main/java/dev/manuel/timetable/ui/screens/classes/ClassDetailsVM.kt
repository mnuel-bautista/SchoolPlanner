package dev.manuel.timetable.ui.screens.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.ClassDetailsFragmentArgs
import dev.manuel.timetable.room.daos.ClassDao
import dev.manuel.timetable.room.daos.TimetableDao
import dev.manuel.timetable.room.entities.ClassWithOccurrences
import dev.manuel.timetable.room.entities.Timetable
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClassDetailsScreenState(
    val classWithOccurrences: ClassWithOccurrences = ClassWithOccurrences(),
    val timetable: Timetable = Timetable(0, ""),
)

@HiltViewModel
class ClassDetailsVM @Inject constructor(
    private val classDao: ClassDao,
    private val timetableDao: TimetableDao,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val mState: MutableStateFlow<ClassDetailsScreenState> =
        MutableStateFlow(ClassDetailsScreenState())

    val state: StateFlow<ClassDetailsScreenState> = mState.asStateFlow()

    var confirmDeletion: Boolean by mutableStateOf(false)
        private set

    private val collectorJob: Job

    init {

        val args = ClassDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
        val classId = args.classId

        collectorJob = viewModelScope.launch {
            classDao.getClassWithOccurrences(classId)
                .collect {
                    val timetable = timetableDao.getTimetable(it.mClass.timetableId)
                    mState.value = ClassDetailsScreenState(it, timetable)
                }
        }
    }

    fun onDeleteClass() {
        confirmDeletion = true
    }

    fun cancelClassDeletion() {
        confirmDeletion = false
    }

    fun confirmClassDeletion() {
        confirmDeletion = false

        viewModelScope.launch {
            collectorJob.cancel()
            classDao.delete(state.value.classWithOccurrences.mClass.id)
            _uiEvent.emit(UIEvent.NavigateBack)
        }
    }

}