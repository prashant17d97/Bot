package com.debugdesk.bot.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.debugdesk.bot.navigation.Screens
import com.debugdesk.bot.repo.RoomRepository
import com.debugdesk.bot.utils.appstate.AppStateManager
import com.debugdesk.bot.utils.enums.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    private val appStateManager: AppStateManager,
    private val roomRepository: RoomRepository
) : ViewModel() {
    val isDynamicColor = appStateManager.isDynamicColorUsing
    val theme = appStateManager.appTheme
    val settingMenus = listOf(
        SettingClicksEvent.Theme,
        SettingClicksEvent.UseDynamicColor(),
        SettingClicksEvent.CreateRemainderProfile(screenRoute = Screens.RemainderProfile.route),
        SettingClicksEvent.Reset
    )


    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            appStateManager.updateTheme(theme = theme)
            _showThemeDropDown.tryEmit(false)
        }
    }

    private val _showThemeDropDown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showThemeDropDown: StateFlow<Boolean> = _showThemeDropDown

    private val _showResetDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog


    fun handleMenuClick(
        settingClicksEvent: SettingClicksEvent,
        navHostController: NavHostController
    ) {
        viewModelScope.launch {
            when (settingClicksEvent) {
                is SettingClicksEvent.CreateRemainderProfile -> navHostController.navigate(
                    settingClicksEvent.screenRoute
                )

                SettingClicksEvent.Theme -> _showThemeDropDown.tryEmit(true)
                is SettingClicksEvent.UseDynamicColor ->
                    appStateManager.useDynamicColors(
                        isDynamicColorUsing = settingClicksEvent.boolean
                    )


                SettingClicksEvent.Reset -> updateResetDialog(true)
            }
        }
    }

    fun updateResetDialog(boolean: Boolean) {
        _showResetDialog.tryEmit(boolean)
    }

    fun reset() {
        viewModelScope.launch {
            updateResetDialog(false)
            appStateManager.clear()
            roomRepository.deleteAllNotes()
            roomRepository.deleteAllStatics()
            appStateManager.getAppPaletteProperty()
        }
    }
}