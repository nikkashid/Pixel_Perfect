package com.nikkashid.pixelperfect.ui.settings

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikkashid.pixelperfect.domain.usecase.AnalyzeVisualQAUseCase
import com.nikkashid.pixelperfect.domain.usecase.GetSettingsItemsUseCase
import com.nikkashid.pixelperfect.ui.settings.model.SettingsSectionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val analyzeVisualQAUseCase: AnalyzeVisualQAUseCase,
    private val getSettingsItemsUseCase: GetSettingsItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Idle)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _settingsSections = MutableStateFlow<List<SettingsSectionData>>(emptyList())
    val settingsSections: StateFlow<List<SettingsSectionData>> = _settingsSections.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _settingsSections.value = getSettingsItemsUseCase()
    }

    fun performScan(screenshot: Bitmap, context: Context) {
        viewModelScope.launch {
            _uiState.value = SettingsUiState.Loading
            try {
                val result = analyzeVisualQAUseCase(screenshot, FIGMA_LINK, context)
                _uiState.value = SettingsUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = SettingsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun dismissOverlay() {
        _uiState.value = SettingsUiState.Idle
    }

    companion object {
        private const val FIGMA_LINK = "https://www.figma.com/proto/31OHtN6hkWaNKkAJ4YGIxA/User-profile---Settings-screen--Community-?node-id=11-2368"
    }
}
