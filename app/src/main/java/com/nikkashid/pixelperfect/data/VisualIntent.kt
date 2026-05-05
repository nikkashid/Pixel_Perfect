package com.nikkashid.pixelperfect.data

import android.graphics.Bitmap

/**
 * Represents the design intent from Figma or other design tools.
 */
data class VisualIntent(
    val componentId: String,
    val figmaNodeId: String,
    val description: String,
    val criticalElements: List<String>,
    val allowedVariance: Float = 0.05f // 5% variance allowed for technical noise
)

/**
 * Result of the AI analysis comparing implementation vs intent.
 */
data class QAAnalysisResult(
    val status: AnalysisStatus,
    val confidence: Float,
    val feedback: String,
    val suggestions: List<String> = emptyList(),
    val implementationImage: Bitmap? = null,
    val designImage: Bitmap? = null
)

enum class AnalysisStatus {
    MATCH,              // Implementation matches design intent
    VISUAL_REGRESSION,  // Genuine UX failure
    TECHNICAL_NOISE,    // Negligible difference (anti-aliasing, etc.)
    UNCERTAIN          // AI needs human intervention
}
