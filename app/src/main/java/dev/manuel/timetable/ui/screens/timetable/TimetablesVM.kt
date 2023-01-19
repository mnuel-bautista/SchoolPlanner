package dev.manuel.timetable.ui.screens.timetable

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.DEFAULT_TIMETABLE_ID
import dev.manuel.timetable.R
import dev.manuel.timetable.SELECTED_TIMETABLE_ID
import dev.manuel.timetable.room.daos.TimetableDao
import dev.manuel.timetable.room.entities.Timetable
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetablesVM @Inject constructor(
    private val preferences: SharedPreferences,
    private val timetableDao: TimetableDao,
) : ViewModel() {

    private val _uiEvent: MutableSharedFlow<UIEvent> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    var timetables: MutableState<List<Timetable>> = mutableStateOf(emptyList())
        private set

    /**
     * Selected timetable by the user.
     * */
    var selected: MutableState<Timetable?> = mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            timetableDao.getAllTimetables()
                .collect { timetables.value = it }
        }

        val timetableId = preferences.getLong(SELECTED_TIMETABLE_ID, DEFAULT_TIMETABLE_ID)

        viewModelScope.launch {
            selected.value = timetableDao.getTimetable(timetableId)
        }
    }

    /**
     * Called when the user confirms that changes for an existing or new timetable
     * must be saved.
     * */
    fun saveChanges(timetable: Timetable) {
        viewModelScope.launch {
            if (timetable.id == 0L) {
                timetableDao.insert(timetable)
            } else {
                timetableDao.update(timetable)
            }
        }
    }

    fun deleteTimetable(timetable: Timetable) {
        viewModelScope.launch {
            val timetableCount = timetables.value.size

            //User must have at least one timetable
            if (timetableCount > 1) {
                val timetables = timetables.value.toMutableList()
                //Delete from the database
                timetableDao.delete(timetable.id)
                //Only update the selected timetable if it is the deleted one.
                if(timetable == selected.value) {
                    //Delete from the temporal list and select the next
                    //timetable.
                    timetables.remove(timetable)
                    val next = timetables.first()
                    selectTimetable(next)
                }
            } else {
                _uiEvent.emit(UIEvent.ShowMessage(R.string.at_least_one_timetable))
            }
        }
    }

    fun selectTimetable(timetable: Timetable) {
        preferences.edit { putLong(SELECTED_TIMETABLE_ID, timetable.id) }
        selected.value = timetable
    }



}