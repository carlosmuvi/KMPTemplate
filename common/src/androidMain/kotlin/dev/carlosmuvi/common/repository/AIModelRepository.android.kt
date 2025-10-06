package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel

/**
 * Android implementation of AIModelRepository
 * Currently no on-device models are available on Android
 */
class AndroidAIModelRepository : AIModelRepository {

    override suspend fun getAvailableModels(): List<AIModel> {
        // No models available on Android yet
        return emptyList()
    }

    override suspend fun getModelById(modelId: String): AIModel? {
        return null
    }
}

actual fun createAIModelRepository(): AIModelRepository {
    return AndroidAIModelRepository()
}