@file:OptIn(ExperimentalNativeApi::class)

package dev.carlosmuvi.common.platform

import dev.carlosmuvi.common.model.Event
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusFullAccess
import platform.EventKit.EKEntityType
import platform.EventKit.EKEvent
import platform.EventKit.EKEventStore
import platform.EventKit.EKSpan
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.experimental.ExperimentalNativeApi

actual class CalendarManager {
    private val eventStore = EKEventStore()

    actual suspend fun hasCalendarPermission(): Boolean {
        val status = EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent)
        return when (status) {
            EKAuthorizationStatusAuthorized -> true
            EKAuthorizationStatusFullAccess -> true
            else -> false
        }
    }

    actual suspend fun requestCalendarPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            eventStore.requestFullAccessToEventsWithCompletion { granted, error ->
                if (error != null) {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                } else {
                    continuation.resume(granted)
                }
            }
        }

    actual suspend fun addEventToCalendar(event: Event) {
        val ekEvent = EKEvent.eventWithEventStore(eventStore)
        ekEvent.title = event.title
        ekEvent.notes = event.description
        ekEvent.location = event.location
        ekEvent.startDate = event.startDateTime.toNSDate()
        ekEvent.endDate = event.endDateTime.toNSDate()
        ekEvent.allDay = event.allDay
        ekEvent.calendar = eventStore.defaultCalendarForNewEvents
            ?: throw IllegalStateException("No default calendar found")
        val result = eventStore.saveEvent(ekEvent, EKSpan.EKSpanThisEvent, null)
        if (!result) throw IllegalStateException("Failed to save event")
    }
}

private fun LocalDateTime.toNSDate(): NSDate {
    val components = NSDateComponents().apply {
        year = this@toNSDate.year.toLong()
        month = this@toNSDate.month.number.toLong()
        day = this@toNSDate.day.toLong()
        hour = this@toNSDate.hour.toLong()
        minute = this@toNSDate.minute.toLong()
        second = this@toNSDate.second.toLong()
    }
    return NSCalendar.currentCalendar.dateFromComponents(components)
        ?: throw IllegalArgumentException("Failed to convert LocalDateTime to NSDate")
}

