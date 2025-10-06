package dev.carlosmuvi.common.model

/**
 * Response from an AI model
 */
data class AIModelResponse(
    val text: String,
    val finishReason: FinishReason = FinishReason.STOP
)

enum class FinishReason {
    STOP,       // Model finished generating naturally
    LENGTH,     // Max length reached
    ERROR       // Error occurred
}

/**
 * Interface for on-device AI models
 */
interface AIModel {
    /**
     * Unique identifier for this model
     */
    val id: String

    /**
     * Human-readable name of the model
     */
    val name: String

    /**
     * Description of the model's capabilities
     */
    val description: String

    /**
     * Run inference on the model with the given prompt
     * @param prompt The input text to process
     * @return The model's response
     */
    suspend fun run(prompt: String): Result<AIModelResponse>
}