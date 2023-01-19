package dev.manuel.timetable.ui.screens

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.DEFAULT_TIMETABLE_ID
import dev.manuel.timetable.SELECTED_TIMETABLE_ID
import dev.manuel.timetable.room.daos.ClassDao
import dev.manuel.timetable.room.daos.TimetableDao
import dev.manuel.timetable.room.entities.OccurrenceWithClass
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableVM @Inject constructor(
    preferences: SharedPreferences,
    private val savedStateHandle: SavedStateHandle,
    private val classDao: ClassDao,
    private val timetableDao: TimetableDao,
) : ViewModel() {

    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            if (key == SELECTED_TIMETABLE_ID) {
                selectedTimetableId.value = preferences.getLong(SELECTED_TIMETABLE_ID, -1)
            }
        }

    /**
     * The id of selected timetable.
     * */
    private val selectedTimetableId: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _timetable: MutableStateFlow<String> = MutableStateFlow("Timetable")

    val timetable: StateFlow<String> = _timetable.asStateFlow()

    private val mClasses: MutableStateFlow<List<OccurrenceWithClass>> = MutableStateFlow(emptyList())

    val courses: StateFlow<List<OccurrenceWithClass>> = mClasses

    init {

        val timetableId = preferences.getLong(SELECTED_TIMETABLE_ID, DEFAULT_TIMETABLE_ID)

        selectedTimetableId.value = timetableId

        viewModelScope.launch {
            selectedTimetableId.collect { id ->
                if(id != DEFAULT_TIMETABLE_ID) {
                    _timetable.value = timetableDao.getTimetable(id).name
                }
            }
        }

        viewModelScope.launch {
            combine(classDao.getOccurrencesWithClass(), selectedTimetableId) { classes, timetable ->
                classes
                    .sortedBy { it.occurrence.starts }
                    .filter { it.mClass.timetableId == timetable }
            }.collect { mClasses.value = it }
        }

        preferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }
}