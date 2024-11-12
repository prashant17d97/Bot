package com.debugdesk.bot.datamodel

import com.debugdesk.bot.utils.timeutil.DisplayMode
import com.debugdesk.bot.utils.timeutil.TimeUnitEnum

data class InitCountDownTimer(
    val timeUnitEnums: List<TimeUnitEnum> = listOf(
        TimeUnitEnum.SECOND,
        TimeUnitEnum.MINUTE,
        TimeUnitEnum.HOUR
    ),
    val displayMode: List<DisplayMode> = listOf(DisplayMode.Ascending, DisplayMode.Descending),
)

data class TimerParameter(
    val timeUnitEnum: TimeUnitEnum = TimeUnitEnum.NOTHING,
    val displayMode: DisplayMode = DisplayMode.NOTHING,
    val timeValue: String = "",
) {

    val time: Int
        get() {
            return if (timeValue.isEmpty()) 0 else timeValue.toInt()
        }
}
