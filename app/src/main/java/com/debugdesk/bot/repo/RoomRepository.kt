package com.debugdesk.bot.repo

import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.Statics

import kotlinx.coroutines.flow.StateFlow

interface RoomRepository {

    val allStatics: StateFlow<List<Statics>>

    val staticsByDate: StateFlow<List<Statics>>

    val allNotes: StateFlow<List<Note>>

    val note: StateFlow<Note>

    fun getAllNotes()

    fun createNote(note: Note)

    fun updateNote(note: Note)

    fun deleteNote(note: Note)

    fun retrieveNoteFromId(noteId: Long)

    fun deleteAllNotes()

    fun getAllStatics()

    fun deleteStatics(statics: Statics)

    fun createStatics(statics: Statics)

    fun updateStatics(statics: Statics)

    fun getStaticsByDate(createdAt: String)

    fun deleteAllStatics()

    fun clearNote()
}
