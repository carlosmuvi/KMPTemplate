package dev.carlosmuvi.common.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

/**
 * Example ViewModel demonstrating the KMP template structure
 */
class MainViewModel : ViewModel(), KoinComponent {

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _message = MutableStateFlow("Hello from Kotlin Multiplatform!")
    val message: StateFlow<String> = _message.asStateFlow()

    fun incrementCounter() {
        _counter.value++
    }

    fun decrementCounter() {
        _counter.value--
    }

    fun updateMessage(newMessage: String) {
        _message.value = newMessage
    }
}
