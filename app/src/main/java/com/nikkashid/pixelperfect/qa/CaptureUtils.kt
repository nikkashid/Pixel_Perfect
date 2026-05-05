package com.nikkashid.pixelperfect.qa

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

object CaptureUtils {

    /**
     * Captures a bitmap of a given View.
     * Note: For Jetpack Compose, capturing a specific composable is often done via
     * GraphicsLayer.record or using the Compose UI Test library.
     */
    fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
