package com.nikkashid.pixelperfect.ui.settings.model

import androidx.annotation.StringRes

data class SettingsSectionData(
    @get:StringRes val titleRes: Int,
    val items: List<SettingsItemData>
)
