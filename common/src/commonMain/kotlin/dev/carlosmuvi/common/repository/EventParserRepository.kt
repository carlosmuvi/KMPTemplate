package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.Event
import dev.carlosmuvi.common.model.ReminderTime
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

/**
 * Repository that parses natural language text into Event objects.
 */
interface EventParserRepository {
    suspend fun parseEvent(text: String): Result<Event>
    suspend fun getAvailableModels(): List<String>
}

/**
 * AI-powered EventParserRepository using on-device models
 */
class AIEventParserRepository(
    private val aiModelRepository: AIModelRepository
) : EventParserRepository {

    override suspend fun parseEvent(text: String): Result<Event> {
        if (text.isBlank()) {
            return Result.failure(IllegalArgumentException("Text cannot be empty"))
        }

        // Get available AI models
        val models = aiModelRepository.getAvailableModels()

        if (models.isEmpty()) {
            return Result.failure(
                IllegalStateException("No AI models available on this platform")
            )
        }

        // Use the first available model
        val model = models.first()

        // Create a structured prompt for event extraction
        val prompt = buildEventExtractionPrompt(text)

        return try {
            val response = model.run(prompt).getOrThrow()
            parseEventFromAIResponse(response.text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailableModels(): List<String> {
        return aiModelRepository.getAvailableModels().map { it.name }
    }

    private fun buildEventExtractionPrompt(text: String): String {
        return """
            Extract event information from the following text and return a JSON object with these fields:
            - title: The event title (required)
            - description: Additional details (optional)
            - location: Event location (optional)
            - startDateTime: ISO format YYYY-MM-DDTHH:MM (required)
            - endDateTime: ISO format YYYY-MM-DDTHH:MM (required)
            - allDay: boolean (required)
            - reminderMinutes: Minutes before event for reminder, 0 for none (optional)

            Text: $text

            Return only the JSON object, no additional text.
        """.trimIndent()
    }

    private fun parseEventFromAIResponse(response: String): Result<Event> {
        return try {
            // For now, create a simple mock event since we don't have actual AI response parsing yet
            // This will be replaced when actual Apple Intelligence API is integrated
            val mockEvent = createMockEventFromText(response)
            Result.success(mockEvent)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createMockEventFromText(text: String): Event {
        // Temporary mock implementation
        val words = text.split(" ").filter { it.isNotBlank() }
        val title = words.take(3).joinToString(" ").ifBlank { "New Event" }

        val startDateTime = LocalDateTime(2025, 10, 6, 14, 0)
        val endDateTime = LocalDateTime(2025, 10, 6, 15, 0)

        return Event(
            title = title,
            description = text,
            location = null,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            allDay = false,
            reminder = ReminderTime.FIFTEEN_MINUTES
        )
    }
}

class MockEventParserRepository : EventParserRepository {

    override suspend fun parseEvent(text: String): Result<Event> {
        // Simulate network/AI delay
        delay(1500)

        // Mock parsing logic - will be replaced with actual AI
        return when {
            text.isBlank() -> Result.failure(
                IllegalArgumentException("Text cannot be empty")
            )

            text.length < 10 -> Result.failure(
                IllegalArgumentException("Text is too short to parse into an event")
            )

            else -> Result.success(createMockedEvent(text))
        }
    }

    override suspend fun getAvailableModels(): List<String> {
        return listOf("Mock AI Model")
    }

    private fun createMockedEvent(text: String): Event {
        // Simple mock: extract first few words as title
        val words = text.split(" ").filter { it.isNotBlank() }
        val title = words.take(3).joinToString(" ")

        // Mock dates - event tomorrow at 2pm for 1 hour
        val startDateTime = LocalDateTime(2025, 10, 6, 14, 0)
        val endDateTime = LocalDateTime(2025, 10, 6, 15, 0)

        return Event(
            title = title.ifBlank { "New Event" },
            description = text,
            location = null,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            allDay = false,
            reminder = ReminderTime.FIFTEEN_MINUTES
        )
    }
}