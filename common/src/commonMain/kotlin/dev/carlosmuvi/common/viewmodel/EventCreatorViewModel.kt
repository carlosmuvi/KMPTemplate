package dev.carlosmuvi.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.carlosmuvi.common.model.EventCreatorState
import dev.carlosmuvi.common.platform.CalendarManager
import dev.carlosmuvi.common.repository.EventParserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventCreatorViewModel : ViewModel(), KoinComponent {

    private val eventParserRepository: EventParserRepository by inject()
    private val calendarManager: CalendarManager by inject()

    private val _state = MutableStateFlow<EventCreatorState>(EventCreatorState.Idle)
    val state: StateFlow<EventCreatorState> = _state.asStateFlow()

    private val _inputText = MutableStateFlow("")

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun parseEvent() {
        val text = _inputText.value

        if (text.isBlank()) {
            _state.value = _state.value.copy(
                error = "Please enter event details"
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            eventParserRepository.parseEvent(text)
                .onSuccess { event ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        event = event
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to parse event"
                    )
                }
        }
    }

    fun resetState() {
        _state.value = EventCreatorState.Idle
        _inputText.value = ""
    }

    fun confirmEvent() {
        val event = _state.value.event ?: return
        addEventToCalendar(event)
    }

    private fun addEventToCalendar(event: dev.carlosmuvi.common.model.Event) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isAddingToCalendar = true,
                error = null,
                successMessage = null
            )

            try {
                // Check and request permission if needed
                val hasPermission = calendarManager.hasCalendarPermission()
                if (!hasPermission) {
                    val granted = calendarManager.requestCalendarPermission()
                    if (!granted) {
                        _state.value = _state.value.copy(
                            isAddingToCalendar = false,
                            error = "Calendar permission is required to add events"
                        )
                        return@launch
                    }
                }

                // Add event to calendar
                calendarManager.addEventToCalendar(event)

                // Show success state briefly, then reset
                _state.value = _state.value.copy(
                    isAddingToCalendar = false,
                    successMessage = "Event added to calendar!"
                )
                kotlinx.coroutines.delay(1500) // Show success for 1.5 seconds
                resetState()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isAddingToCalendar = false,
                    error = e.message ?: "Failed to add event to calendar"
                )
            }
        }
    }
}