package dev.manuel.timetable

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.room.daos.ClassDao
import dev.manuel.timetable.room.entities.Class
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val classDao: ClassDao,
    private val preferences: SharedPreferences,
) : ViewModel() {

    private val mClasses: MutableStateFlow<List<Class>> = MutableStateFlow(emptyList())

    /**
     * The first five classes in the currently selected timetable
     * */
    val classes: StateFlow<List<Class>> = mClasses.asStateFlow()

    private val timetableId: MutableStateFlow<Long> = MutableStateFlow(0L)

    private val preferencesListener: OnSharedPreferenceChangeListener =
        OnSharedPreferenceChangeListener { preferences, key ->
            if (key == SELECTED_TIMETABLE_ID) {
                timetableId.value = preferences.getLong(key, 0L)
            }
        }

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)
        timetableId.value = preferences.getLong(SELECTED_TIMETABLE_ID, DEFAULT_TIMETABLE_ID)

        viewModelScope.launch {
            timetableId.collect { timetableId ->
                classDao.getAllClassesByTimetableFlow(timetableId).collect {
                    mClasses.value = it.take(5)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        preferences.unregisterOnSharedPreferenceChangeListener(preferencesListener)
    }

}