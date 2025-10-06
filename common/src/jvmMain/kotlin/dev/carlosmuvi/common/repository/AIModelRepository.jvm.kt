package dev.carlosmuvi.common.repository

import dev.carlosmuvi.common.model.AIModel

/**
 * JVM implementation of AIModelRepository
 * Currently no on-device models are available on JVM
 */
class JvmAIModelRepository : AIModelRepository {

    override suspend fun getAvailableModels(): List<AIModel> {
        // No models available on JVM yet
        return emptyList()
    }

    override suspend fun getModelById(modelId: String): AIModel? {
        return null
    }
}

actual fun createAIModelRepository(): AIModelRepository {
    return JvmAIModelRepository()
}
