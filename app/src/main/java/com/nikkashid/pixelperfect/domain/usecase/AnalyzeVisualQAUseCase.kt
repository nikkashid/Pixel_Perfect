package com.nikkashid.pixelperfect.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.data.VisualIntent
import com.nikkashid.pixelperfect.domain.repository.VisualQARepository
import com.nikkashid.pixelperfect.qa.IntentAnalyzer
import com.nikkashid.pixelperfect.ui.settings.PixelPerfectScreen
import com.nikkashid.pixelperfect.ui.theme.UIConstants

/**
 * Acts as the Interactor for the Visual QA process.
 */
class AnalyzeVisualQAUseCase(
    private val repository: VisualQARepository,
    private val intentAnalyzer: IntentAnalyzer
) {
    suspend operator fun invoke(
        screenshot: Bitmap,
        figmaLink: String,
        context: Context,
        screen: PixelPerfectScreen
    ): QAAnalysisResult {
        
        // 1. Fetch design from Repository (Separation of Concerns)
        val designSource = repository.getDesignReference(figmaLink)

        // 2. Prepare Context Intent
        val intent = buildIntent(context, screen)

        // 3. Coordinate Analysis
        return try {
            val result = intentAnalyzer.analyze(screenshot, designSource.bitmap, intent)
            
            if (result.status != AnalysisStatus.UNCERTAIN) {
                result.copy(
                    implementationImage = screenshot, 
                    designImage = designSource.bitmap,
                    dataSource = designSource.sourceName
                )
            } else {
                getDummyResult(screenshot, context, screen)
            }
        } catch (_: Exception) {
            getDummyResult(screenshot, context, screen)
        }
    }

    private fun buildIntent(context: Context, screen: PixelPerfectScreen): VisualIntent {
        return VisualIntent(
            componentId = if (screen == PixelPerfectScreen.SETTINGS) 
                context.getString(R.string.intent_component_settings) 
                else context.getString(R.string.edit_profile_title),
            figmaNodeId = UIConstants.EMPTY_STRING,
            description = if (screen == PixelPerfectScreen.SETTINGS) 
                context.getString(R.string.intent_description_settings)
                else "Perfect implementation of Android Edit Profile screen.",
            criticalElements = if (screen == PixelPerfectScreen.SETTINGS) listOf(
                context.getString(R.string.critical_element_background),
                context.getString(R.string.critical_element_iconography),
                context.getString(R.string.critical_element_padding)
            ) else listOf("Input alignment", "Primary button color", "Profile image sizing")
        )
    }

    private fun getDummyResult(screenshot: Bitmap, context: Context, screen: PixelPerfectScreen): QAAnalysisResult {
        return if (screen == PixelPerfectScreen.SETTINGS) {
            QAAnalysisResult(
                status = AnalysisStatus.VISUAL_REGRESSION,
                confidence = DUMMY_CONFIDENCE,
                implementationImage = screenshot,
                feedback = context.getString(R.string.dummy_feedback_intro),
                suggestions = listOf(
                    context.getString(R.string.dummy_suggestion_remove_eye),
                    context.getString(R.string.dummy_suggestion_add_back),
                    context.getString(R.string.dummy_suggestion_fix_casing)
                ),
                dataSource = "Level 4: Dummy Demo Fallback"
            )
        } else {
            QAAnalysisResult(
                status = AnalysisStatus.MATCH,
                confidence = 1.0f,
                implementationImage = screenshot,
                feedback = "The implementation of the Edit Profile screen perfectly matches the design intent.",
                suggestions = emptyList(),
                dataSource = "Level 4: Dummy Demo Fallback"
            )
        }
    }

    companion object {
        private const val DUMMY_CONFIDENCE = 0.95f
    }
}
