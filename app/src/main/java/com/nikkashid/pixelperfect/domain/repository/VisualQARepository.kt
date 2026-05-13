package com.nikkashid.pixelperfect.domain.repository

import android.graphics.Bitmap

data class DesignSource(
    val bitmap: Bitmap?,
    val sourceName: String
)

interface VisualQARepository {
    suspend fun getDesignReference(figmaLink: String): DesignSource
}
