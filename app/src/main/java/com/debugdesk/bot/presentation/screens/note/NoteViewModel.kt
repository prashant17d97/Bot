package com.debugdesk.bot.presentation.screens.note

import androidx.lifecycle.ViewModel
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.repo.RoomRepository

class NoteViewModel(
    private val roomRepository: RoomRepository,
) : ViewModel() {

    val note = roomRepository.note

    fun updateNote(note: Note) {
        roomRepository.updateNote(note)
    }

    fun deleteNote(note: Note) {
        roomRepository.deleteNote(note)
    }

    fun retrieveNoteFromId(id: Long) {
        roomRepository.retrieveNoteFromId(id)
    }

    fun clearNote() {
        roomRepository.clearNote()
    }
}