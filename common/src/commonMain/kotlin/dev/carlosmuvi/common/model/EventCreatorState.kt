package dev.carlosmuvi.common.model

/**
 * State for the Event Creator screen
 */
data class EventCreatorState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val error: String? = null,
    val isAddingToCalendar: Boolean = false,
    val successMessage: String? = null
) {
    companion object {
        val Idle = EventCreatorState()
    }
}
