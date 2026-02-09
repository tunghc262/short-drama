package com.shortdrama.movie.views.bases.ext

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toMonthDayOrdinal(): String {
    val date = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
    val day = date.dayOfMonth
    val ordinal = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
    return "${monthFormatter.format(date)} $day$ordinal"
}

fun Long.toDayMonthNumeric(): String {
    val date = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())
        .format(date)
}

