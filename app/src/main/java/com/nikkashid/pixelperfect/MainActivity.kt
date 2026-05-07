package com.nikkashid.pixelperfect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nikkashid.pixelperfect.domain.usecase.AnalyzeVisualQAUseCase
import com.nikkashid.pixelperfect.domain.usecase.GetSettingsItemsUseCase
import com.nikkashid.pixelperfect.qa.FigmaService
import com.nikkashid.pixelperfect.qa.IntentAnalyzer
import com.nikkashid.pixelperfect.ui.PixelPerfectApp
import com.nikkashid.pixelperfect.ui.settings.SettingsViewModel
import com.nikkashid.pixelperfect.ui.theme.PixelPerfectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual DI for Hackathon - Injecting dependencies into the ViewModel
        val figmaService = FigmaService(BuildConfig.FIGMA_TOKEN)
        val intentAnalyzer = IntentAnalyzer(BuildConfig.GEMINI_API_KEY)
        val analyzeUseCase = AnalyzeVisualQAUseCase(figmaService, intentAnalyzer)
        val getItemsUseCase = GetSettingsItemsUseCase()
        val viewModel = SettingsViewModel(analyzeUseCase, getItemsUseCase)

        enableEdgeToEdge()
        setContent {
            PixelPerfectTheme {
                PixelPerfectApp(viewModel)
            }
        }
    }
}
