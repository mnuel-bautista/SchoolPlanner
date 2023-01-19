package dev.manuel.timetable.ui.screens.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.room.daos.ClassDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.manuel.timetable.room.entities.Class as MClass

@HiltViewModel
class ClassesVM @Inject constructor (
    private val classDao: ClassDao,
) : ViewModel() {

    private val mClasses: MutableStateFlow<List<MClass>> = MutableStateFlow(emptyList())

    val classes: StateFlow<List<MClass>> = mClasses

    init {
        viewModelScope.launch {
            classDao.getAllClassesFlow().collect { mClasses.value = it }
        }
    }

}