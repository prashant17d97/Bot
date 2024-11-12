package com.debugdesk.bot.presentation.screens.tracker

import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.emptyNote
import com.debugdesk.bot.utils.timeutil.Time
import com.debugdesk.bot.utils.timeutil.TimerState
import com.debugdesk.bot.utils.timeutil.getCurrentTime
import com.debugdesk.bot.enums.NoteEditorButton

data class TrackerModel(
    val showBSM: Boolean = false,
    val showDigitalTimer: Boolean = false,
    val note: Note = emptyNote,
    val notes: List<Note> = emptyList(),
    val clock: Time = getCurrentTime(),
    val countdownTimer: Time = getCurrentTime(),
    val positiveButton: NoteEditorButton = NoteEditorButton.SAVE,
    val timerState: TimerState = TimerState.NotInitialized,
)
