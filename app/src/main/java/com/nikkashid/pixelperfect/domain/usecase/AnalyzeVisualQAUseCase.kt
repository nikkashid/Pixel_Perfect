package com.nikkashid.pixelperfect.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.data.VisualIntent
import com.nikkashid.pixelperfect.qa.FigmaService
import com.nikkashid.pixelperfect.qa.FigmaUrlParser
import com.nikkashid.pixelperfect.qa.IntentAnalyzer
import com.nikkashid.pixelperfect.ui.theme.UIConstants

class AnalyzeVisualQAUseCase(
    private val figmaService: FigmaService,
    private val intentAnalyzer: IntentAnalyzer
) {
    suspend operator fun invoke(
        screenshot: Bitmap,
        figmaLink: String,
        context: Context
    ): QAAnalysisResult {
        // 1. Parse Link
        val linkData = FigmaUrlParser.parse(figmaLink)
        
        // 2. Fetch Design (Level 1)
        var figmaDesign = if (linkData != null && linkData.nodeId != null) {
            figmaService.fetchComponentImage(linkData.fileKey, linkData.nodeId)
        } else null

        // 3. Local Fallback (Level 2)
        if (figmaDesign == null) {
            figmaDesign = BitmapFactory.decodeResource(context.resources, R.drawable.figma_reference)
        }

        // 4. Analyze (Level 3 & 4 handled inside analyzer)
        val intent = VisualIntent(
            componentId = context.getString(R.string.intent_component_settings),
            figmaNodeId = linkData?.nodeId ?: UIConstants.EMPTY_STRING,
            description = context.getString(R.string.intent_description_settings),
            criticalElements = listOf(
                context.getString(R.string.critical_element_background),
                context.getString(R.string.critical_element_iconography),
                context.getString(R.string.critical_element_padding)
            )
        )

        return try {
            val result = intentAnalyzer.analyze(screenshot, figmaDesign, intent)
            if (result.status != AnalysisStatus.UNCERTAIN) {
                result.copy(implementationImage = screenshot, designImage = figmaDesign)
            } else {
                getDummyResult(screenshot, context)
            }
        } catch (_: Exception) {
            getDummyResult(screenshot, context)
        }
    }

    private fun getDummyResult(screenshot: Bitmap, context: Context): QAAnalysisResult {
        return QAAnalysisResult(
            status = AnalysisStatus.VISUAL_REGRESSION,
            confidence = DUMMY_CONFIDENCE,
            implementationImage = screenshot,
            feedback = context.getString(R.string.dummy_feedback_intro),
            suggestions = listOf(
                context.getString(R.string.dummy_suggestion_remove_eye),
                context.getString(R.string.dummy_suggestion_add_back),
                context.getString(R.string.dummy_suggestion_fix_casing)
            )
        )
    }

    companion object {
        private const val DUMMY_CONFIDENCE = 0.95f
    }
}
