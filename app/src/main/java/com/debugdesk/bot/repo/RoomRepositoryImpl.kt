package com.debugdesk.bot.repo

import android.util.Log
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.Statics
import com.debugdesk.bot.datamodel.emptyNote
import com.debugdesk.bot.db.AppDatabase
import com.debugdesk.bot.utils.timeutil.getCurrentUtcTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomRepositoryImpl(private val appDatabase: AppDatabase) : RoomRepository {

    private val _allStatics: MutableStateFlow<List<Statics>> = MutableStateFlow(emptyList())
    override val allStatics: StateFlow<List<Statics>> = _allStatics

    private val _staticsByDate: MutableStateFlow<List<Statics>> = MutableStateFlow(emptyList())
    override val staticsByDate: StateFlow<List<Statics>> = _staticsByDate

    private val _allNotes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())
    override val allNotes: StateFlow<List<Note>> = _allNotes

    private val _note: MutableStateFlow<Note> = MutableStateFlow(emptyNote)

    override val note: StateFlow<Note> = _note

    private val scope = CoroutineScope(Dispatchers.IO)


    companion object {
        private const val TAG = "RoomRepositoryImpl"
    }


    init {
        getAllNotes()
        getAllStatics()
    }

    /**
     * **Notes**
     * */
    override fun getAllNotes() {
        scope.launch {
            try {
                _allNotes.tryEmit(appDatabase.notesDao().getAllNotes())
            } catch (ex: Exception) {
                _allNotes.tryEmit(emptyList())
            }
        }

    }

    override fun createNote(note: Note) {
        scope.launch {
            try {
                appDatabase.notesDao().insert(note.copy(createdAt = getCurrentUtcTime()))
                getAllNotes()
            } catch (e: Exception) {
                Log.e(TAG, "insertNote: ${e.message}")
                getAllNotes()
            }
        }
    }

    override fun updateNote(note: Note) {
        scope.launch {
            try {
                appDatabase.notesDao().update(note)
                retrieveNoteFromId(noteId = note.id)
            } catch (e: Exception) {
                Log.e(TAG, "updateNote: ${e.message}")
            }
        }
    }

    override fun deleteNote(note: Note) {
        scope.launch {
            try {
                appDatabase.notesDao().delete(note)
            } catch (e: Exception) {
                Log.e(TAG, "deleteNote: ${e.message}")
                getAllNotes()
            }
        }
    }

    override fun retrieveNoteFromId(noteId: Long) {
        scope.launch {
            try {
                val note = appDatabase.notesDao().retrieveNoteFromId(noteId)
                _note.tryEmit(note)

            } catch (e: Exception) {
                // Handle or log the exception
                Log.e(TAG, "retrieveNoteFromIdException: ${e.message}")

            }
        }
    }

    override fun deleteAllNotes() {
        scope.launch {
            try {
                appDatabase.notesDao().deleteAllNotes()
                getAllNotes()
            } catch (e: Exception) {
                getAllNotes()
            }
        }
    }


    /**
     * **Statics**
     * */
    override fun getAllStatics() {
        scope.launch {
            try {
                _allStatics.tryEmit(appDatabase.staticsDao().getAll())
            } catch (e: Exception) {
                // Handle or log the exception
                _allStatics.value = emptyList()
            }
        }
    }

    override fun deleteStatics(statics: Statics) {
        scope.launch {
            try {
                appDatabase.staticsDao().delete(statics)
                getAllStatics()
            } catch (e: Exception) {
                // Handle or log the exception
                getAllStatics()
            }
        }
    }

    override fun createStatics(statics: Statics) {
        scope.launch {
            try {
                appDatabase.staticsDao().create(statics)
                getAllStatics()
            } catch (e: Exception) {
                // Handle or log the exception
                getAllStatics()
            }
        }
    }

    override fun updateStatics(statics: Statics) {
        scope.launch {
            try {
                appDatabase.staticsDao().update(statics)
                getAllStatics()
            } catch (e: Exception) {
                // Handle or log the exception
                getAllStatics()
            }
        }
    }

    override fun getStaticsByDate(createdAt: String) {
        scope.launch {
            try {
                _staticsByDate.tryEmit(appDatabase.staticsDao().getByDate(createdAt))
            } catch (e: Exception) {
                // Handle or log the exception
                _staticsByDate.tryEmit(emptyList())
            }
        }
    }

    override fun deleteAllStatics() {
        scope.launch {
            try {
                appDatabase.staticsDao().deleteAllStatics()
                getAllStatics()
            } catch (e: Exception) {
                // Handle or log the exception
                getAllStatics()
            }
        }
    }

    override fun clearNote() {
        _note.tryEmit(emptyNote)
    }
}

