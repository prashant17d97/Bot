package com.debugdesk.bot.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.debugdesk.bot.datamodel.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "statics_table")
@TypeConverters(NoteListConverter::class)
data class Statics(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val createdAt: String,
    val finishedAt: String,
    val notes: List<Note>,
    val startedAt: String,
    val totalTime: Long
)



@TypeConverters
class NoteListConverter {
    // Convert List<Note> to a format that Room can store in the database
    // You can customize this based on your requirements
    @TypeConverter
    fun fromNoteList(notes: List<Note>): String {
        return Gson().toJson(notes)
    }

    // Convert the stored format back to List<Note>
    // You can customize this based on your requirements
    @TypeConverter
    fun toNoteList(notesString: String): List<Note> {
        val type = object : TypeToken<List<Note>>() {}.type
        return Gson().fromJson(notesString, type)
    }
}


val emptyStatics = Statics(
    id = 0,
    createdAt = "",
    finishedAt = "",
    notes = emptyList(),
    startedAt = "",
    totalTime = 0
)


