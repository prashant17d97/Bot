package com.debugdesk.bot.utils.timeutil


import android.icu.util.Calendar
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun calculateRemainingTime(targetTimeMillis: Long): Time {
    val currentTimeMillis = System.currentTimeMillis()

    return if (currentTimeMillis >= targetTimeMillis) {
        getCurrentTime()
    } else {
        val remainingMillis = targetTimeMillis - currentTimeMillis
        remainingMillis.getCalculatedTime().copy(format = "")
    }
}


fun willFinishIn(targetTimeMillis: Long, milliSeconds: Long): Time {
    val currentTime = System.currentTimeMillis()

    return if (currentTime >= targetTimeMillis) {
        getCurrentTime()
    } else {
        milliSeconds.getCalculatedTime().copy(format = "")
    }
}


fun getCurrentTime(): Time {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    val amPm = when (calendar.get(Calendar.AM_PM)) {
        Calendar.AM -> "AM"
        Calendar.PM -> "PM"
        else -> "Unknown"
    }

    return Time(
        hour = 12.takeIf { hour == 0 } ?: hour,
        minute = minute,
        second = second,
        format = amPm,
        isFinished = true
    )
}

fun Long.getCalculatedTime(): Time {
    return Time(
        hour = TimeUnit.MILLISECONDS.toHours(this).toInt(),
        minute = (TimeUnit.MILLISECONDS.toMinutes(this) % 60).toInt(),
        second = (TimeUnit.MILLISECONDS.toSeconds(this) % 60).toInt(),
        format = getCurrentTime().format,
        isFinished = false
    )
}

fun getMilliSecondWithCurrentTime(timeUnit: TimeUnitEnum, duration: Int): Long {
    return System.currentTimeMillis() + getMilliSeconds(timeUnit, duration)
}

fun getMilliSeconds(timeUnit: TimeUnitEnum, duration: Int): Long {
    return duration * timeUnit.millisecondsPerUnit
}

fun TimeUnit.timeUnitToLong(): Long = when (this) {
    TimeUnit.NANOSECONDS -> TimeUnitEnum.NANOSECOND.millisecondsPerUnit
    TimeUnit.MICROSECONDS -> TimeUnitEnum.MICROSECOND.millisecondsPerUnit
    TimeUnit.MILLISECONDS -> TimeUnitEnum.MILLISECOND.millisecondsPerUnit
    TimeUnit.SECONDS -> TimeUnitEnum.SECOND.millisecondsPerUnit
    TimeUnit.MINUTES -> TimeUnitEnum.MINUTE.millisecondsPerUnit
    TimeUnit.HOURS -> TimeUnitEnum.HOUR.millisecondsPerUnit
    TimeUnit.DAYS -> TimeUnitEnum.DAY.millisecondsPerUnit
}

fun Time.toMilliSecond(): Long {
    require(hour >= 0) { "Hours must be non-negative" }
    require(minute in 0 until 60) { "Minutes must be between 0 and 59" }
    require(second in 0 until 60) { "Seconds must be between 0 and 59" }

    val millisecondsInHour = 3600000L
    val millisecondsInMinute = 60000L
    val millisecondsInSecond = 1000L

    return hour * millisecondsInHour + minute * millisecondsInMinute + second * millisecondsInSecond

}


fun getCurrentUtcTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneOffset.UTC)

    val currentUtcTime = Instant.now()

    return formatter.format(currentUtcTime)
}

fun String.convertDateTimeFormat(): String {
    val inputFormatter = DateTimeFormatter.ISO_INSTANT
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' h:mm a")

    val parsedDateTime = Instant.from(inputFormatter.parse(this))
    val localDateTime = parsedDateTime.atZone(ZoneId.systemDefault())

    return outputFormatter.format(localDateTime)
}

fun main() {
    println(getCurrentUtcTime())
    println(getCurrentUtcTime().convertDateTimeFormat())
}
