package com.debugdesk.bot.navigation


const val BOT_APP_HOME = "BotAppHome"
const val NOTE_VIEW = "NoteView"
const val REMAINDER_PROFILE = "RemainderProfile"

/**
 * Arguments Ids*/
const val noteId = "noteId"

sealed class Screens(val route: String, val name: String = route) {
    data object BotAppHome : Screens(BOT_APP_HOME)
    data object NoteView : Screens("$NOTE_VIEW/{$noteId}") {
        fun passNoteId(noteId: Long): String {
            return "$NOTE_VIEW/$noteId"
        }
    }
    data object RemainderProfile: Screens(REMAINDER_PROFILE)

}