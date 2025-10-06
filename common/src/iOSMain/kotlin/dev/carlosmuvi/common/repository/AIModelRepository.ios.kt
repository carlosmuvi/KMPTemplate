package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.model.AIModelResponse
import dev.carlosmuvi.common.model.FinishReason
import platform.Foundation.NSString
import platform.

/**
 * iOS implementation of AIModelRepository
 */
class IOSAIModelRepository : AIModelRepository {

    override suspend fun getAvailableModels(): List<AIModel> {
        return listOf(AppleFoundationModel())
    }

    override suspend fun getModelById(modelId: String): AIModel? {
        return getAvailableModels().find { it.id == modelId }
    }
}

/**
 * Apple Foundation Model implementation
 * Uses Apple Intelligence LanguageModelSession API via Swift interop
 */
class AppleFoundationModel : AIModel {
    override val id: String = "apple-foundation"
    override val name: String = "Apple Foundation Model"
    override val description: String = "Apple's on-device foundation model for text generation and understanding"

    private val instructions = """
        You are an assistant that extracts event information from natural language text.
        Extract the following information and return a JSON object:
        - title: The event title (required)
        - description: Additional details (optional)
        - location: Event location (optional)
        - startDateTime: ISO format YYYY-MM-DDTHH:MM (required)
        - endDateTime: ISO format YYYY-MM-DDTHH:MM (required)
        - allDay: boolean (required)
        - reminderMinutes: Minutes before event for reminder, 0 for none (optional)

        Return only the JSON object, no additional text or formatting.
    """.trimIndent()

    override suspend fun run(prompt: String): Result<AIModelResponse> {
        return try {
            val a: NSString
            val response = runLanguageModel(prompt)
            Result.success(
                AIModelResponse(
                    text = response,
                    finishReason = FinishReason.STOP
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun runLanguageModel(prompt: String): String {
        // Mock response for now
        return "Mock AI response: Event extracted from '$prompt'"
    }
}

actual fun createAIModelRepository(): AIModelRepository {
    return IOSAIModelRepository()
}