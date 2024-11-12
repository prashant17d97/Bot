package com.debugdesk.bot.presentation.screens.botapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debugdesk.bot.utils.appstate.AppStateManager
import com.debugdesk.bot.utils.appstate.AtabActions
import com.debugdesk.bot.utils.enums.BottomBar
import kotlinx.coroutines.launch

open class BotAppViewModel(
    private val appStateManager: AppStateManager,
) : ViewModel() {

    val theme = appStateManager.appTheme
    val isDynamicColorUsing = appStateManager.isDynamicColorUsing
    val bottomTab: List<BottomBar> = listOf(
        BottomBar.Statics,
        BottomBar.Tracker,
        BottomBar.Settings,
    )

    val isScrollGuideShown = appStateManager.isScrollGuideShown

    fun init() {
        viewModelScope.launch {
            appStateManager.getAppPaletteProperty()
        }
    }

    suspend fun scrollGuideShown() {
        if (!isScrollGuideShown.value) {
            appStateManager.scrollGuideShown(true)
        }
    }

}