package dev.carlosmuvi.kmptemplate.ai

import android.content.Context
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.content
import com.google.ai.edge.aicore.generationConfig
import dev.carlosmuvi.common.model.AIInstructions
import dev.carlosmuvi.common.model.AIModel
import dev.carlosmuvi.common.model.AIModelResponse
import dev.carlosmuvi.common.model.FinishReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Gemini Nano Model implementation for Android
 * Uses Google's on-device Gemini Nano model via AICore
 */
class GeminiNanoModel(
    private val context: Context
) : AIModel {
    override val id: String = "gemini-nano"
    override val name: String = "Gemini Nano"
    override val description: String = "Google's on-device foundation model for text generation"

    private val instructions = AIInstructions.eventExtraction

    private val generationConfig = generationConfig {
        context = this@GeminiNanoModel.context
        temperature = 0.7f
        topK = 40
        maxOutputTokens = 1024
    }

    private val generativeModel = GenerativeModel(
        generationConfig = generationConfig
    )

    override suspend fun run(prompt: String): AIModelResponse {
        val response = runLanguageModel(prompt)
        return AIModelResponse(
            text = response,
            finishReason = FinishReason.STOP
        )
    }

    private suspend fun runLanguageModel(prompt: String): String {
        return withContext(Dispatchers.IO) {
            try {
                // Create content with instructions and prompt
                val input = content {
                    text(instructions)
                    text(prompt)
                }

                val response = generativeModel.generateContent(input)
                response.text ?: throw IllegalStateException("Empty response from Gemini Nano")
            } catch (e: Exception) {
                throw RuntimeException("Failed to generate response from Gemini Nano: ${e.message}", e)
            }
        }
    }
}
