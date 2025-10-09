package dev.carlosmuvi.common.viewmodel

import androidx.lifecycle.ViewModel
import dev.carlosmuvi.common.platform.PlatformInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Example ViewModel demonstrating the KMP template structure
 */
class MainViewModel : ViewModel(), KoinComponent {

    // Example: Injecting PlatformInfo from Swift on iOS
    private val platformInfo: PlatformInfo by inject()

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _message = MutableStateFlow("Hello from Kotlin Multiplatform!")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _platformDetails = MutableStateFlow("")
    val platformDetails: StateFlow<String> = _platformDetails.asStateFlow()

    init {
        // Example usage of Swift-provided dependency
        _platformDetails.value = "${platformInfo.getPlatformName()} ${platformInfo.getOSVersion()}"
    }

    fun incrementCounter() {
        _counter.value++
    }

    fun decrementCounter() {
        _counter.value--
    }

}
