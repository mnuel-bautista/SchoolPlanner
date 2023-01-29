package dev.manuel.timetable.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.room.daos.NoteDao
import dev.manuel.timetable.room.entities.NoteWithClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesVM @Inject constructor(
    private val noteDao: NoteDao
): ViewModel() {

    private val _notesWithClass: MutableStateFlow<List<NoteWithClass>> = MutableStateFlow(emptyList())

    val notesWithClass: StateFlow<List<NoteWithClass>> = _notesWithClass.asStateFlow()

    init {
        viewModelScope.launch {
            noteDao.getNotesWithClass().collect {
                _notesWithClass.value = it.filter { nwc -> nwc.note != null }
            }
        }
    }

}