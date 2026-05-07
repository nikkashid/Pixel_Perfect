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
        // Updated Prompt matching the one provided in the mail
        val prompt = """
            I am providing two images: 'design.png' (Figma source) and 'reality.png' (current build). 
            Compare them for visual discrepancies in layout, padding, font-weight, and color. 
            Output a JSON list of defects. 
            
            Context: This is a ${intent.componentId}. 
            Intent Description: ${intent.description}
            
            Format your response for a human developer with:
            STATUS: [MATCH / VISUAL_REGRESSION / TECHNICAL_NOISE]
            FEEDBACK: A detailed breakdown of discrepancies and perfectly implemented sections, focusing on actionable feedback.
        """.trimIndent()

        return try {
            val response = model.generateContent(
                content {
                    image(figmaDesign) // design.png
                    image(screenshot)  // reality.png
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
