package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel

/**
 * Repository for accessing platform-specific on-device AI models
 */
interface AIModelRepository {
    /**
     * Get a list of all available AI models on this platform
     * @return List of available AIModel instances
     */
    suspend fun getAvailableModels(): List<AIModel>

    /**
     * Get a specific model by its ID
     * @param modelId The ID of the model to retrieve
     * @return The model if found, null otherwise
     */
    suspend fun getModelById(modelId: String): AIModel?
}

/**
 * Factory function to create a platform-specific AIModelRepository
 */
expect fun createAIModelRepository(): AIModelRepository