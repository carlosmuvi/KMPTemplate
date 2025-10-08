package dev.carlosmuvi.common.platform

import dev.carlosmuvi.common.model.Event

/**
 * Platform-specific calendar manager
 * Provides calendar integration capabilities across platforms
 */
expect class CalendarManager {
    /**
     * Check if the app has permission to access the calendar
     * @return true if permission is granted, false otherwise
     */
    suspend fun hasCalendarPermission(): Boolean

    /**
     * Request calendar permission from the user
     * @return true if permission was granted, false otherwise
     */
    suspend fun requestCalendarPermission(): Boolean

    /**
     * Add an event to the system calendar
     * @param event The event to add
     * @throws SecurityException if calendar permission is not granted
     * @throws IllegalStateException if the operation fails
     */
    suspend fun addEventToCalendar(event: Event)
}
