package com.nikkashid.pixelperfect.qa

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Service to interact with Figma REST API.
 */
class FigmaService(private val apiToken: String) {

    private val client = OkHttpClient()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    companion object {
        private const val BASE_URL = "https://api.figma.com/v1"
        private const val IMAGES_ENDPOINT = "images"
        private const val HEADER_FIGMA_TOKEN = "X-Figma-Token"
        
        // Query Parameters
        private const val PARAM_IDS = "ids"
        private const val PARAM_FORMAT = "format"
        private const val PARAM_SCALE = "scale"
        
        private const val DEFAULT_FORMAT = "png"
        private const val DEFAULT_SCALE = "2"
    }

    /**
     * Fetches the rendered image from Figma for a specific node.
     */
    suspend fun fetchComponentImage(fileKey: String, nodeId: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            // 1. Build the Figma API URL safely using HttpUrl.Builder
            val url = BASE_URL.toHttpUrl().newBuilder()
                .addPathSegment(IMAGES_ENDPOINT)
                .addPathSegment(fileKey)
                .addQueryParameter(PARAM_IDS, nodeId)
                .addQueryParameter(PARAM_FORMAT, DEFAULT_FORMAT)
                .addQueryParameter(PARAM_SCALE, DEFAULT_SCALE)
                .build()

            val request = Request.Builder()
                .url(url)
                .addHeader(HEADER_FIGMA_TOKEN, apiToken)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null
            
            val body = response.body?.string() ?: return@withContext null
            val responseJson = jsonParser.parseToJsonElement(body).jsonObject
            
            // Extract the image URL for the specific node
            val images = responseJson["images"]?.jsonObject ?: return@withContext null
            val imageUrl = images[nodeId]?.jsonPrimitive?.content ?: return@withContext null

            // 2. Download the actual image
            val imageRequest = Request.Builder().url(imageUrl).build()
            val imageResponse = client.newCall(imageRequest).execute()
            if (!imageResponse.isSuccessful) return@withContext null
            
            val imageBytes = imageResponse.body?.bytes() ?: return@withContext null
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
