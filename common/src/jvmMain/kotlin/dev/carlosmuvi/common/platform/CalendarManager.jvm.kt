package dev.carlosmuvi.common.platform

import dev.carlosmuvi.common.model.Event

actual class CalendarManager {
    actual suspend fun hasCalendarPermission(): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun requestCalendarPermission(): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun addEventToCalendar(event: Event) {
    }
}