@file:OptIn(kotlin.time.ExperimentalTime::class)

package dev.carlosmuvi.common.platform

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import dev.carlosmuvi.common.model.Event
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.TimeZone as JavaTimeZone

actual class CalendarManager(private val context: Context) {

    actual suspend fun hasCalendarPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    actual suspend fun requestCalendarPermission(): Boolean {
        // Permission request is handled by the UI layer in Android
        // This method returns the current permission status
        return hasCalendarPermission()
    }

    actual suspend fun addEventToCalendar(event: Event) {
        if (!hasCalendarPermission()) {
            throw SecurityException("Calendar permission not granted")
        }

        val contentResolver = context.contentResolver
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, event.startDateTime.toEpochMillis())
            put(CalendarContract.Events.DTEND, event.endDateTime.toEpochMillis())
            put(CalendarContract.Events.TITLE, event.title)
            put(CalendarContract.Events.DESCRIPTION, event.description ?: "")
            put(CalendarContract.Events.EVENT_LOCATION, event.location ?: "")
            put(CalendarContract.Events.CALENDAR_ID, getDefaultCalendarId())
            put(CalendarContract.Events.EVENT_TIMEZONE, JavaTimeZone.getDefault().id)
            put(CalendarContract.Events.ALL_DAY, if (event.allDay) 1 else 0)
        }

        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            ?: throw IllegalStateException("Failed to insert event into calendar")
    }

    private fun getDefaultCalendarId(): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.VISIBLE} = 1",
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getLong(0)
            }
        }

        throw IllegalStateException("No calendar found")
    }

    private fun LocalDateTime.toEpochMillis(): Long {
        return this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
