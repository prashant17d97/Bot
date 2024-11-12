package com.debugdesk.bot.utils.appstate

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.debugdesk.bot.datastore.DataStore
import com.debugdesk.bot.utils.enums.Theme
import com.debugdesk.bot.utils.extensions.CommonFunctions.toTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

class AppStateManagerImpl(
    private val dataStore: DataStore
) : AppStateManager {

    private val _appTheme: MutableStateFlow<Theme> = MutableStateFlow(Theme.Dark)
    override val appTheme: StateFlow<Theme> = _appTheme

    private val _isDynamicColorUsing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isDynamicColorUsing: StateFlow<Boolean> = _isDynamicColorUsing

    private val _isScrollGuide: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isScrollGuideShown: StateFlow<Boolean> = _isScrollGuide

    override suspend fun updateTheme(theme: Theme) {
        _appTheme.tryEmit(theme)
        saveTheme(theme)
    }

    override suspend fun useDynamicColors(isDynamicColorUsing: Boolean) {
        _isDynamicColorUsing.tryEmit(isDynamicColorUsing)
        saveDynamicColor(isDynamicColorUsing)
    }


    override suspend fun getAppPaletteProperty() {
        _isDynamicColorUsing.tryEmit(getDynamicColor())
        _appTheme.tryEmit(getTheme())
        _isScrollGuide.tryEmit(getGuide())
    }

    override suspend fun getTheme(): Theme {
        return dataStore.getString(THEME).first().toTheme()
    }

    override suspend fun getDynamicColor(): Boolean {
        return dataStore.getBoolean(DYNAMIC_COLOR, true).first()
    }

    override suspend fun saveTheme(theme: Theme) {
        dataStore.set(THEME, theme.name)
    }

    override suspend fun saveDynamicColor(boolean: Boolean) {
        dataStore.set(DYNAMIC_COLOR, boolean)
    }

    override suspend fun scrollGuideShown(boolean: Boolean) {
        dataStore.set(SCROLL_GUIDE, boolean)
        _isScrollGuide.tryEmit(getGuide())
    }

    override suspend fun getGuide(): Boolean {
        return dataStore.getBoolean(SCROLL_GUIDE).first()
    }


    override suspend fun clear() {
        dataStore.clear()
    }


    companion object {
        private val THEME = stringPreferencesKey("THEME")
        private val DYNAMIC_COLOR = booleanPreferencesKey("DYNAMIC_COLOR")
        private val SCROLL_GUIDE = booleanPreferencesKey("SCROLL_GUIDE")
    }

}