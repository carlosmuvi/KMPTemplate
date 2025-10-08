package dev.carlosmuvi.common.platform

/**
 * Interface for language model providers across platforms
 */
interface LanguageModelProvider {
    suspend fun runModel(instructions: String, prompt: String): String
}
