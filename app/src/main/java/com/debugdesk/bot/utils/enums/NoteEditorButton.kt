package com.debugdesk.bot.enums

import androidx.annotation.StringRes
import com.debugdesk.bot.R

enum class NoteEditorButton(@StringRes val stringId: Int) {
    SAVE(R.string.save),
    UPDATE(R.string.update),
    CREATE(R.string.create)
}