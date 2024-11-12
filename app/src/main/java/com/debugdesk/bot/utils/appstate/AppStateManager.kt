package com.debugdesk.bot.utils.appstate

import com.debugdesk.bot.utils.enums.Theme
import kotlinx.coroutines.flow.StateFlow

interface AppStateManager {

    val appTheme: StateFlow<Theme>

    val isScrollGuideShown: StateFlow<Boolean>

    val isDynamicColorUsing: StateFlow<Boolean>

    suspend fun updateTheme(theme: Theme)

    suspend fun useDynamicColors(isDynamicColorUsing: Boolean)

    suspend fun getAppPaletteProperty()

    suspend fun getTheme(): Theme

    suspend fun getGuide(): Boolean

    suspend fun getDynamicColor(): Boolean

    suspend fun saveTheme(theme: Theme)

    suspend fun saveDynamicColor(boolean: Boolean)

    suspend fun scrollGuideShown(boolean: Boolean)

    suspend fun clear()

}