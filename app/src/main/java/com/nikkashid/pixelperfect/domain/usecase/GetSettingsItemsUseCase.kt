package com.nikkashid.pixelperfect.domain.usecase

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Security
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.ui.settings.model.SettingsItemData
import com.nikkashid.pixelperfect.ui.settings.model.SettingsSectionData

class GetSettingsItemsUseCase {
    operator fun invoke(): List<SettingsSectionData> {
        return listOf(
            SettingsSectionData(
                titleRes = R.string.settings_section_account,
                items = listOf(
                    SettingsItemData(Icons.Default.PersonOutline, R.string.settings_item_edit_profile),
                    SettingsItemData(Icons.Default.Security, R.string.settings_item_security),
                    SettingsItemData(Icons.Default.NotificationsNone, R.string.settings_item_notifications),
                    SettingsItemData(Icons.Default.LockOpen, R.string.settings_item_privacy)
                )
            ),
            SettingsSectionData(
                titleRes = R.string.settings_section_support,
                items = listOf(
                    SettingsItemData(Icons.Default.CreditCard, R.string.settings_item_subscription),
                    SettingsItemData(Icons.AutoMirrored.Filled.Help, R.string.settings_item_help),
                    SettingsItemData(Icons.Default.Info, R.string.settings_item_terms)
                )
            ),
            SettingsSectionData(
                titleRes = R.string.settings_section_cache,
                items = listOf(
                    SettingsItemData(Icons.Default.DeleteOutline, R.string.settings_item_free_space),
                    SettingsItemData(Icons.Default.DataUsage, R.string.settings_item_data_saver)
                )
            )
        )
    }
}
