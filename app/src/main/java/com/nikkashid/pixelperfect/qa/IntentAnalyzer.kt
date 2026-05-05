package com.nikkashid.pixelperfect.qa

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.data.VisualIntent

/**
 * The core engine of PixelPerfect AI. 
 * Uses Gemini 1.5 Pro to compare implementation vs design.
 */
class IntentAnalyzer(private val apiKey: String) {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = apiKey
    )

    suspend fun analyze(
        screenshot: Bitmap,
        figmaDesign: Bitmap,
        intent: VisualIntent
    ): QAAnalysisResult {
        val prompt = """
            Compare these two images of a UI component (${intent.componentId}).
            Image 1: The actual Android implementation.
            Image 2: The design source of truth from Figma.
            
            Intent Description: ${intent.description}
            
            Task:
            1. Identify visual differences (color, spacing, typography).
            2. Classify if the difference is 'TECHNICAL_NOISE' (anti-aliasing, sub-pixel shifts) or a 'VISUAL_REGRESSION'.
            3. Provide specific feedback for the developer.
            
            Format your response as:
            STATUS: [MATCH / VISUAL_REGRESSION / TECHNICAL_NOISE]
            FEEDBACK: [Reasoning]
        """.trimIndent()

        return try {
            val response = model.generateContent(
                content {
                    image(screenshot)
                    image(figmaDesign)
                    text(prompt)
                }
            )
            
            val text = response.text ?: ""
            parseAiResponse(text)
        } catch (e: Exception) {
            QAAnalysisResult(
                status = AnalysisStatus.UNCERTAIN,
                confidence = 0f,
                feedback = "AI Analysis failed: ${e.message}"
            )
        }
    }

    private fun parseAiResponse(text: String): QAAnalysisResult {
        val status = when {
            text.contains("MATCH") -> AnalysisStatus.MATCH
            text.contains("TECHNICAL_NOISE") -> AnalysisStatus.TECHNICAL_NOISE
            text.contains("VISUAL_REGRESSION") -> AnalysisStatus.VISUAL_REGRESSION
            else -> AnalysisStatus.UNCERTAIN
        }
        
        return QAAnalysisResult(
            status = status,
            confidence = 0.9f,
            feedback = text.substringAfter("FEEDBACK:").trim()
        )
    }
}
