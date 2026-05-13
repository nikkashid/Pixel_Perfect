package com.nikkashid.pixelperfect.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.domain.repository.DesignSource
import com.nikkashid.pixelperfect.domain.repository.VisualQARepository
import com.nikkashid.pixelperfect.qa.FigmaService
import com.nikkashid.pixelperfect.qa.FigmaUrlParser

class VisualQARepositoryImpl(
    private val context: Context,
    private val figmaService: FigmaService
) : VisualQARepository {

    override suspend fun getDesignReference(figmaLink: String): DesignSource {
        // Level 1: Live Figma API
        val linkData = FigmaUrlParser.parse(figmaLink)
        if (linkData != null && linkData.nodeId != null) {
            val liveImage = figmaService.fetchComponentImage(linkData.fileKey, linkData.nodeId)
            if (liveImage != null) {
                return DesignSource(liveImage, "Level 1: Live Figma API")
            }
        }

        // Level 2: Local Fallback
        val localImage = BitmapFactory.decodeResource(context.resources, R.drawable.figma_reference)
        if (localImage != null) {
            return DesignSource(localImage, "Level 2: Local Reference Image")
        }

        // Level 3: No Design (General Audit)
        return DesignSource(null, "Level 3: General AI Audit")
    }
}
