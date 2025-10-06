package dev.carlosmuvi.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.carlosmuvi.common.model.Event
import dev.carlosmuvi.common.model.UiState
import dev.carlosmuvi.common.repository.EventParserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventCreatorViewModel : ViewModel(), KoinComponent {

    private val eventParserRepository: EventParserRepository by inject()

    private val _eventState = MutableStateFlow<UiState<Event>>(UiState.Idle)
    val eventState: StateFlow<UiState<Event>> = _eventState.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun parseEvent() {
        val text = _inputText.value

        if (text.isBlank()) {
            _eventState.value = UiState.Error("Please enter event details")
            return
        }

        viewModelScope.launch {
            _eventState.value = UiState.Loading

            eventParserRepository.parseEvent(text)
                .onSuccess { event ->
                    _eventState.value = UiState.Success(event)
                }
                .onFailure { error ->
                    _eventState.value = UiState.Error(
                        message = error.message ?: "Failed to parse event",
                        throwable = error
                    )
                }
        }
    }

    fun resetState() {
        _eventState.value = UiState.Idle
        _inputText.value = ""
    }

    fun confirmEvent() {
        // TODO: Implement actual calendar integration
        // For now, just reset the state
        resetState()
    }
}