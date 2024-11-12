package com.debugdesk.bot.utils.enums

import androidx.annotation.StringRes
import com.debugdesk.bot.R

enum class Theme(@StringRes val stringId: Int) {
    SystemDefault(stringId = R.string.system_default),
    Dark(stringId = R.string.dark),
    Light(stringId = R.string.light),
}