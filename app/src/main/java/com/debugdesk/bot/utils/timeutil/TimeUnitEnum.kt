package com.debugdesk.bot.utils.timeutil

import androidx.annotation.StringRes
import com.debugdesk.bot.R

enum class TimeUnitEnum(val millisecondsPerUnit: Long = 0L, @StringRes val textValue: Int = 0) {
    MILLISECOND(1L),
    NANOSECOND(1L / 1_000_000L),
    MICROSECOND(1L / 1_000L),
    SECOND(millisecondsPerUnit = 1000L, textValue = R.string.seconds),
    MINUTE(millisecondsPerUnit = 60L * SECOND.millisecondsPerUnit, textValue = R.string.minutes),
    HOUR(millisecondsPerUnit = 60L * MINUTE.millisecondsPerUnit, textValue = R.string.hours),
    DAY(millisecondsPerUnit = 24L * HOUR.millisecondsPerUnit, textValue = R.string.days),
    WEEK(millisecondsPerUnit = 7L * DAY.millisecondsPerUnit, textValue = R.string.weeks),

    // Note: This is a rough estimate, months can vary
    MONTH(millisecondsPerUnit = 30L * DAY.millisecondsPerUnit, textValue = R.string.months),

    NOTHING
}


enum class DisplayMode(@StringRes val textValue: Int = 0) {
    Ascending(R.string.ascending),
    Descending(R.string.descending),
    NOTHING
}

enum class TimerState {
    NotInitialized,
    Started,
    Paused,
    Cancel,
    Replay,
    Resume,
    Finished
}