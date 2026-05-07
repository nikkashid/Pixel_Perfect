package com.nikkashid.pixelperfect.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.ui.settings.model.SettingsItemData
import com.nikkashid.pixelperfect.ui.settings.model.SettingsSectionData
import com.nikkashid.pixelperfect.ui.theme.SettingsCardBackground
import com.nikkashid.pixelperfect.ui.theme.UIConstants

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val sections by viewModel.settingsSections.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(UIConstants.MediumSpacing)
    ) {
        sections.forEach { section ->
            SettingsSection(section)
            Spacer(modifier = Modifier.height(UIConstants.LargeSpacing))
        }
        
        Text(
            text = stringResource(R.string.settings_actions_label),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SettingsSection(section: SettingsSectionData) {
    Column {
        Text(
            text = stringResource(section.titleRes),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = UIConstants.SmallSpacing)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SettingsCardBackground, RoundedCornerShape(UIConstants.DefaultCornerRadius))
                .padding(vertical = UIConstants.TinySpacing)
        ) {
            section.items.forEach { item ->
                SettingsItem(item)
            }
        }
    }
}

@Composable
fun SettingsItem(item: SettingsItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = UIConstants.MediumSpacing, vertical = UIConstants.IntermediateSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(item.icon, contentDescription = null, modifier = Modifier.size(UIConstants.StandardIconSize), tint = Color.Black)
        Spacer(modifier = Modifier.width(UIConstants.MediumSpacing))
        Text(text = stringResource(item.labelRes), fontSize = 16.sp, color = Color.Black)
    }
}
