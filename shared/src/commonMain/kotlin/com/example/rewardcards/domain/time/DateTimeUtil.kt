package com.example.rewardcards.domain.time

import kotlinx.datetime.*

object DateTimeUtil {

    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun formatCardDate(dateTime: LocalDateTime): String {
        val dayOfWeek = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = dateTime.dayOfMonth
        val year = dateTime.year

        return buildString {
            append(dayOfWeek)
            append(", ")
            append(month)
            append(" ")
            append(day)
            append(", ")
            append(year)
        }
    }

    fun getTime(dateTime: LocalDateTime): String {
        val hour = if (dateTime.hour % 12 == 0) 12 else dateTime.hour % 12
        val minute = if(dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute
        val amPm = if (dateTime.hour < 12) " a.m." else " p.m."

        return buildString {
            append(hour)
            append(":")
            append(minute)
            append(amPm)
        }
    }
}