package com.debugdesk.bot.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.debugdesk.bot.utils.timeutil.convertDateTimeFormat

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val heading: String = "",
    val description: String = "",
    val createdAt: String = ""
) {
    val date: String
        get() = if (createdAt.isNotEmpty()) {
            createdAt.convertDateTimeFormat()
        } else ""
}

val emptyNote = Note(id = 0, heading = "", description = "")

