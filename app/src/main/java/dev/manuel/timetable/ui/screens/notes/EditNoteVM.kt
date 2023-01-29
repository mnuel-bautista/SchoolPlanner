package dev.manuel.timetable.ui.screens.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.manuel.timetable.R
import dev.manuel.timetable.room.daos.NoteDao
import dev.manuel.timetable.room.entities.NoteWithClass
import dev.manuel.timetable.ui.UIEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val noteDao: NoteDao,
) : ViewModel() {

    private val _uiEvent: MutableSharedFlow<UIEvent> = MutableSharedFlow()

    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    private val _noteWithClass: MutableStateFlow<NoteWithClass> = MutableStateFlow(NoteWithClass())

    val noteWithClass: StateFlow<NoteWithClass> = _noteWithClass.asStateFlow()

    init {

        val args = EditNoteFragmentArgs.fromSavedStateHandle(savedStateHandle)
        val noteId = args.noteId

        viewModelScope.launch {
            if(noteId != 0L) {
                _noteWithClass.value = noteDao.getNoteWithClassById(noteId)
            }
        }
    }


    fun changeTitle(title: String) {
        val note = _noteWithClass.value.note
        _noteWithClass.value = noteWithClass.value.copy(
            note = note?.copy(title = title)
        )
    }

    fun changeContent(content: String) {
        val note = _noteWithClass.value.note
        _noteWithClass.value = noteWithClass.value.copy(
            note = note?.copy(content = content)
        )
    }

    fun changeClass(mClass: dev.manuel.timetable.room.entities.Class) {
        val note = _noteWithClass.value.note
        _noteWithClass.value = noteWithClass.value.copy(
            mClass = mClass,
            note = note?.copy(classId = mClass.id)
        )
    }

    fun markAsFavorite() {
        val note = _noteWithClass.value.note
        _noteWithClass.value = noteWithClass.value.copy(
            note = note?.copy(isFavorite = !note.isFavorite)
        )
    }

    fun pinNote() {
        val note = _noteWithClass.value.note
        _noteWithClass.value = noteWithClass.value.copy(
            note = note?.copy(isPinned = !note.isPinned)
        )
    }

    fun deleteNote() {
        viewModelScope.launch {
            noteWithClass.value.note?.let { noteDao.delete(it.id) }
        }
    }


    fun saveChanges() {
        viewModelScope.launch {
            if(noteWithClass.value.note?.id == 0L) {
                noteDao.insert(noteWithClass.value.note!!)
                _uiEvent.emit(UIEvent.ShowMessage(R.string.note_added))
                _uiEvent.emit(UIEvent.NavigateBack)
            } else {
                noteWithClass.value.note?.let { noteDao.update(it) }
                _uiEvent.emit(UIEvent.NavigateBack)
            }
        }
    }

}

