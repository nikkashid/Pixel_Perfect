package com.nikkashid.pixelperfect.qa

import android.graphics.Bitmap
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.data.VisualIntent

class IntentAnalyzer(apiKey: String) {

    private val cleanApiKey = apiKey.trim().removeSurrounding("\"")

    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
    )

    private val modelConfigs = listOf(
        Pair("gemini-2.0-flash", "v1beta"),
        Pair("gemini-flash-latest", "v1beta"),
        Pair("gemini-1.5-flash", "v1beta")
    )

    suspend fun analyze(
        screenshot: Bitmap,
        figmaDesign: Bitmap?,
        intent: VisualIntent
    ): QAAnalysisResult {
        
        if (!cleanApiKey.startsWith("AIza")) {
            return QAAnalysisResult(AnalysisStatus.UNCERTAIN, 0f, "ERROR: Invalid API Key format.")
        }

        var lastError = ""
        
        for (config in modelConfigs) {
            try {
                val model = GenerativeModel(
                    modelName = config.first,
                    apiKey = cleanApiKey,
                    safetySettings = safetySettings,
                    requestOptions = RequestOptions(apiVersion = config.second)
                )

                // Prompt optimized for clean, professional output without markdown stars
                val prompt = if (figmaDesign != null) {
                    """
                        You are a senior Android QA Engineer. Compare 'design.png' (Figma source) and 'reality.png' (current build) for visual discrepancies in layout, padding, font-weight, and color.
                        
                        Context: ${intent.componentId}
                        Intent: ${intent.description}
                        
                        CRITICAL INSTRUCTIONS:
                        1. Do NOT use markdown symbols like stars (**) or hashtags (#).
                        2. Use professional, plain text headings.
                        3. Provide specific, actionable feedback for a developer.
                        
                        Response Format:
                        STATUS: [MATCH / VISUAL_REGRESSION / TECHNICAL_NOISE]
                        FEEDBACK: [Your detailed analysis]
                    """.trimIndent()
                } else {
                    """
                        Analyze 'reality.png' (Android Settings screen implementation).
                        Check for text casing consistency and Material 3 iconography.
                        
                        CRITICAL INSTRUCTIONS:
                        1. Do NOT use markdown symbols like stars (**) or hashtags (#).
                        2. Use professional, plain text.
                        
                        Response Format:
                        STATUS: [VISUAL_REGRESSION]
                        FEEDBACK: [Your detailed analysis]
                    """.trimIndent()
                }

                val response = model.generateContent(
                    content {
                        figmaDesign?.let { image(it) }
                        image(screenshot)
                        text(prompt)
                    }
                )

                val text = response.text ?: continue
                return parseAiResponse(text)
                
            } catch (e: Exception) {
                lastError = e.message ?: "Unknown"
                Log.e("PixelPerfect", "Model ${config.first} failed: $lastError")
            }
        }

        return QAAnalysisResult(
            status = AnalysisStatus.UNCERTAIN,
            confidence = 0f,
            feedback = "AI Analysis Error: $lastError"
        )
    }

    private fun parseAiResponse(text: String): QAAnalysisResult {
        val status = when {
            text.contains("MATCH") -> AnalysisStatus.MATCH
            text.contains("TECHNICAL_NOISE") -> AnalysisStatus.TECHNICAL_NOISE
            text.contains("VISUAL_REGRESSION") -> AnalysisStatus.VISUAL_REGRESSION
            else -> AnalysisStatus.VISUAL_REGRESSION
        }
        
        // Clean up any remaining markdown stars just in case the AI ignores instructions
        val cleanFeedback = text.substringAfter("FEEDBACK:")
            .replace("**", "")
            .replace("#", "")
            .trim()
            .ifEmpty { text }
            
        return QAAnalysisResult(
            status = status,
            confidence = 0.95f,
            feedback = cleanFeedback
        )
    }
}
