package com.debugdesk.bot.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.debugdesk.bot.datamodel.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun retrieveNoteFromId(noteId: Long): Note

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()
}
