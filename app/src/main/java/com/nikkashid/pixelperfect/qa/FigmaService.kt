package com.nikkashid.pixelperfect.qa

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Service to interact with Figma REST API.
 */
class FigmaService(private val apiToken: String) {

    private val client = OkHttpClient()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    /**
     * Fetches the rendered image from Figma for a specific node.
     */
    suspend fun fetchComponentImage(fileKey: String, nodeId: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            // 1. Get the image URL from Figma API
            val request = Request.Builder()
                .url("https://api.figma.com/v1/images/$fileKey?ids=$nodeId&format=png&scale=2")
                .addHeader("X-Figma-Token", apiToken)
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
