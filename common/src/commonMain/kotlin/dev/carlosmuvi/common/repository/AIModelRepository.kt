package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.model.AIModels

/**
 * Repository for accessing on-device AI models
 * Models are injected during Koin initialization
 */
class AIModelRepository(
    private val aiModels: AIModels
) {
    /**
     * Get a list of all available AI models
     * @return List of available AIModel instances
     */
    fun getAvailableModels(): List<AIModel> = aiModels.getAll()

    /**
     * Get a specific model by its ID
     * @param modelId The ID of the model to retrieve
     * @return The model if found, null otherwise
     */
    fun getModelById(modelId: String): AIModel? = aiModels.getById(modelId)
}