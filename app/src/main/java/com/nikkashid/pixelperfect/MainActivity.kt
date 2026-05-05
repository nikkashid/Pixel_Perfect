package com.nikkashid.pixelperfect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nikkashid.pixelperfect.ui.theme.PixelPerfectTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.qa.VisualQAOverlay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixelPerfectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PixelPerfectDemo(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PixelPerfectDemo(modifier: Modifier = Modifier) {
    var analysisResult by remember { mutableStateOf<QAAnalysisResult?>(null) }
    var showOverlay by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Greeting(name = "PixelPerfect AI")

        Spacer(modifier = Modifier.height(24.dp))

        // This button represents a UI component that we want to verify
        Button(onClick = {
            // Simulate AI QA Check with images
            analysisResult = QAAnalysisResult(
                status = AnalysisStatus.VISUAL_REGRESSION,
                confidence = 0.89f,
                feedback = "Font weight for 'Hello' is 'Bold' in design but appears as 'Regular' in implementation. Margin-top is 2px off.",
                suggestions = listOf(
                    "Change font-weight to 700 (Bold)",
                    "Increase vertical spacing by 2dp"
                )
            )
            showOverlay = true
        }) {
            Text("Verify Component Intent")
        }

        if (showOverlay && analysisResult != null) {
            VisualQAOverlay(
                result = analysisResult!!,
                onDismiss = { showOverlay = false }
            )
        }

        analysisResult?.let { result ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "QA Result: ${result.status}",
                style = MaterialTheme.typography.headlineSmall,
                color = if (result.status == AnalysisStatus.MATCH || result.status == AnalysisStatus.TECHNICAL_NOISE) 
                    MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Text(
                text = result.feedback,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PixelPerfectTheme {
        Greeting("Android")
    }
}