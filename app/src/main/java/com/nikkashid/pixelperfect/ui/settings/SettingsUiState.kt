package com.nikkashid.pixelperfect.ui.settings

import com.nikkashid.pixelperfect.data.QAAnalysisResult

sealed interface SettingsUiState {
    object Idle : SettingsUiState
    object Loading : SettingsUiState
    data class Success(val result: QAAnalysisResult) : SettingsUiState
    data class Error(val message: String) : SettingsUiState
}
