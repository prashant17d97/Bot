package com.debugdesk.bot.presentation.screens.settings

import android.os.Build
import androidx.annotation.StringRes
import com.debugdesk.bot.R

sealed class SettingClicksEvent(@StringRes val menuText: Int, val isVisible: Boolean = true) {
    data object Theme : SettingClicksEvent(menuText = R.string.theme)
    data object Reset : SettingClicksEvent(menuText = R.string.reset)
    data class UseDynamicColor(val boolean: Boolean = true) :
        SettingClicksEvent(
            menuText = R.string.useDynamicColor,
            isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        )

    data class CreateRemainderProfile(val screenRoute: String) :
        SettingClicksEvent(menuText = R.string.create_remainder_profile)
}

