package dev.carlosmuvi.common.model

import kotlinx.datetime.LocalDateTime

/**
 * Represents an iCal event with all standard properties
 */
data class Event(
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val allDay: Boolean = false,
    val reminder: ReminderTime? = null
)

enum class ReminderTime(val minutes: Int) {
    NONE(0),
    AT_TIME(0),
    FIVE_MINUTES(5),
    FIFTEEN_MINUTES(15),
    THIRTY_MINUTES(30),
    ONE_HOUR(60),
    ONE_DAY(1440)
}