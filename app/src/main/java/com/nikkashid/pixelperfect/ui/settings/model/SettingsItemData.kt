package com.nikkashid.pixelperfect.ui.settings.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsItemData(
    val icon: ImageVector,
    @get:StringRes val labelRes: Int
)
