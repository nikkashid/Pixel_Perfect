package com.nikkashid.pixelperfect.qa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult

@Composable
fun VisualQAOverlay(
    result: QAAnalysisResult,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Visual QA Analysis",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Text("X") // Simple close button
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (result.status) {
                            AnalysisStatus.MATCH, AnalysisStatus.TECHNICAL_NOISE -> Color(0xFFE8F5E9)
                            AnalysisStatus.VISUAL_REGRESSION -> Color(0xFFFFEBEE)
                            else -> Color(0xFFF5F5F5)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Status: ${result.status}",
                            style = MaterialTheme.typography.titleLarge,
                            color = when (result.status) {
                                AnalysisStatus.MATCH, AnalysisStatus.TECHNICAL_NOISE -> Color(0xFF2E7D32)
                                AnalysisStatus.VISUAL_REGRESSION -> Color(0xFFC62828)
                                else -> Color.Black
                            }
                        )
                        Text(
                            text = "Confidence: ${(result.confidence * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Side-by-Side Comparison
                Text(
                    text = "Comparison (Design vs Implementation)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Figma Design", style = MaterialTheme.typography.labelMedium)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.LightGray)
                        ) {
                            result.designImage?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Figma Design",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            } ?: Text("No Design Image", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "App Implementation", style = MaterialTheme.typography.labelMedium)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.LightGray)
                        ) {
                            result.implementationImage?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "App Implementation",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            } ?: Text("No App Image", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // AI Feedback
                Text(
                    text = "AI Insights",
                    style = MaterialTheme.typography.titleMedium
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = result.feedback,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (result.suggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Suggestions", style = MaterialTheme.typography.titleMedium)
                    result.suggestions.forEach { suggestion ->
                        Text(
                            text = "• $suggestion",
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to App")
                }
            }
        }
    }
}
