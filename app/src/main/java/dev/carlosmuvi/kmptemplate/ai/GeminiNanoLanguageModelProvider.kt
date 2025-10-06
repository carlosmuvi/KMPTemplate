package dev.carlosmuvi.kmptemplate.ai

import android.content.Context
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.content
import com.google.ai.edge.aicore.generationConfig
import dev.carlosmuvi.common.platform.LanguageModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of LanguageModelProvider using Gemini Nano
 * Uses Google's on-device Gemini Nano model via AICore
 */
class GeminiNanoLanguageModelProvider(
    private val context: Context
) : LanguageModelProvider {

    private val generationConfig = generationConfig {
        context = this@GeminiNanoLanguageModelProvider.context
        temperature = 0.7f
        topK = 40
        maxOutputTokens = 1024
    }

    private val generativeModel = GenerativeModel(
        generationConfig = generationConfig
    )

    override suspend fun runModel(instructions: String, prompt: String): String {
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
