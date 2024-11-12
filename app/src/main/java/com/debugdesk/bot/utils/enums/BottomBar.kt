package com.debugdesk.bot.utils.enums

import com.debugdesk.bot.R

enum class BottomBar(val stringId: Int, val iconId: Int) {
    Statics(stringId = R.string.statics, iconId = R.drawable.statics),
    Tracker(stringId = R.string.tracker, iconId = R.drawable.clock),
    Settings(stringId = R.string.settings, iconId = R.drawable.icon_setting),
}