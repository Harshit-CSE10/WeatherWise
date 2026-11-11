package com.weatherwise.app.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateTimeUtils {
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
    private val fullFormatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault())

    fun formatTime(epochSeconds: Long, timezoneOffsetSeconds: Int = 0): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds))
        return instant.atZone(zoneId).format(timeFormatter)
    }

    fun formatDay(epochSeconds: Long): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val localDate = instant.atZone(ZoneId.systemDefault())
        val today = LocalDate.now()
        return when (localDate.toLocalDate()) {
            today -> "Today"
            today.plusDays(1) -> "Tomorrow"
            else -> localDate.format(fullFormatter)
        }
    }

    fun formatHour(epochSeconds: Long, timezoneOffsetSeconds: Int = 0): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds))
        return instant.atZone(zoneId).format(DateTimeFormatter.ofPattern("ha", Locale.getDefault()))
    }

    fun epochToLocalDate(epochSeconds: Long, timezoneOffsetSeconds: Int = 0): LocalDate {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds))
        return instant.atZone(zoneId).toLocalDate()
    }
}
