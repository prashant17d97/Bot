package com.debugdesk.bot.utils.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.utils.enums.Theme
import com.debugdesk.bot.utils.enums.Theme.Dark
import com.debugdesk.bot.utils.enums.Theme.Light
import com.debugdesk.bot.utils.enums.Theme.SystemDefault

object CommonFunctions {
    fun Note.trim(): Note {
        return Note(
            id = id,
            heading = heading.trim(),
            description = description.trim(),
            createdAt = createdAt
        )
    }

    fun String.toTheme(): Theme {
        return when (this) {
            SystemDefault.name -> SystemDefault
            Dark.name -> Dark
            Light.name -> Light
            else -> Dark
        }
    }
}