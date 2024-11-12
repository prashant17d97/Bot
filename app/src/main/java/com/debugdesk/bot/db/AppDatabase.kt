package com.debugdesk.bot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.db.dao.NoteDao
import com.debugdesk.bot.db.dao.StaticsDao
import com.debugdesk.bot.datamodel.Statics

@Database(entities = [Statics::class, Note::class,], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun staticsDao(): StaticsDao
    abstract fun notesDao(): NoteDao
}