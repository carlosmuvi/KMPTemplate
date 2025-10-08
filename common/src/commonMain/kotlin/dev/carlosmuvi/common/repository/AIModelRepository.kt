package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel

/**
 * Repository for accessing on-device AI models
 * Models are injected during Koin initialization
 */
class AIModelRepository(
    private val models: List<AIModel>
) {
    /**
     * Get a list of all available AI models
     * @return List of available AIModel instances
     */
    suspend fun getAvailableModels(): List<AIModel> = models

    /**
     * Get a specific model by its ID
     * @param modelId The ID of the model to retrieve
     * @return The model if found, null otherwise
     */
    suspend fun getModelById(modelId: String): AIModel? =
        models.find { it.id == modelId }
}