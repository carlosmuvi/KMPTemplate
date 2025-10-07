@file:OptIn(ExperimentalTime::class)

package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIInstructions
import dev.carlosmuvi.common.model.Event
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
            val response = model.run(prompt)
            parseEventFromAIResponse(response.text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAvailableModels(): List<String> {
        return aiModelRepository.getAvailableModels().map { it.name }
    }

    private fun buildEventExtractionPrompt(text: String): String {
        // Get current date and time as string
        val currentDateTime = Clock.System.now().toString()
        return """
            ${AIInstructions.eventExtraction}

            Current date and time: $currentDateTime
            Use this as reference when interpreting relative dates like "today", "tomorrow", "tonight", etc.

            Text to parse: $text
        """.trimIndent()
    }

    @Serializable
    private data class EventJson(
        val title: String,
        val description: String? = null,
        val location: String? = null,
        val startDateTime: String,
        val endDateTime: String,
        val allDay: Boolean,
    )

    private val json = Json { ignoreUnknownKeys = true }

    private fun parseEventFromAIResponse(response: String): Result<Event> {
        return try {
            // Extract JSON from response (handle markdown code blocks)
            val jsonText = extractJsonFromResponse(response)

            // Parse JSON using kotlinx.serialization
            val eventJson = json.decodeFromString<EventJson>(jsonText)

            // Convert to Event model
            val event = eventJson.toEvent()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Failed to parse AI response: ${e.message}", e))
        }
    }

    private fun extractJsonFromResponse(response: String): String {
        // Remove markdown code blocks if present
        var json = response.trim()

        // Check if wrapped in ```json ... ```
        if (json.startsWith("```json")) {
            json = json.removePrefix("```json").trim()
        } else if (json.startsWith("```")) {
            json = json.removePrefix("```").trim()
        }

        if (json.endsWith("```")) {
            json = json.removeSuffix("```").trim()
        }

        return json
    }

    private fun EventJson.toEvent(): Event {
        // Parse ISO datetime strings (YYYY-MM-DDTHH:MM)
        fun parseDateTime(str: String): LocalDateTime {
            val parts = str.split("T")
            val dateParts = parts[0].split("-")
            val timeParts = parts.getOrNull(1)?.split(":") ?: listOf("0", "0")

            return LocalDateTime(
                year = dateParts[0].toInt(),
                monthNumber = dateParts[1].toInt(),
                dayOfMonth = dateParts[2].toInt(),
                hour = timeParts[0].toInt(),
                minute = timeParts[1].toInt()
            )
        }

        return Event(
            title = title,
            description = description,
            location = location,
            startDateTime = parseDateTime(startDateTime),
            endDateTime = parseDateTime(endDateTime),
            allDay = allDay
        )
    }
}
