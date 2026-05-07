package com.nikkashid.pixelperfect.qa

import androidx.core.net.toUri

object FigmaUrlParser {
    /**
     * Parses a Figma URL to extract the file key and node ID.
     * Support URLs like: https://www.figma.com/design/FILE_KEY/NAME?node-id=NODE_ID
     */
    fun parse(url: String): FigmaLinkData? {
        return try {
            val uri = url.toUri()
            val segments = uri.pathSegments
            
            // Expected path: /design/FILE_KEY/NAME or /file/FILE_KEY/NAME or /proto/FILE_KEY/NAME
            if (segments.size < 2) return null
            
            val fileKey = segments[1]
            var nodeId = uri.getQueryParameter("node-id")
            
            // Figma URL uses '-' instead of ':' in the query param
            nodeId = nodeId?.replace("-", ":")
            
            if (fileKey.isNotEmpty()) {
                FigmaLinkData(fileKey, nodeId)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

data class FigmaLinkData(
    val fileKey: String,
    val nodeId: String?
)
