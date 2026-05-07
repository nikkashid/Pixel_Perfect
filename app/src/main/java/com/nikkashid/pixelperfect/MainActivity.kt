package com.nikkashid.pixelperfect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikkashid.pixelperfect.data.AnalysisStatus
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.data.VisualIntent
import com.nikkashid.pixelperfect.qa.CaptureUtils
import com.nikkashid.pixelperfect.qa.FigmaService
import com.nikkashid.pixelperfect.qa.FigmaUrlParser
import com.nikkashid.pixelperfect.qa.IntentAnalyzer
import com.nikkashid.pixelperfect.qa.VisualQAOverlay
import com.nikkashid.pixelperfect.ui.theme.PixelPerfectTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixelPerfectTheme {
                PixelPerfectApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelPerfectApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var analysisResult by remember { mutableStateOf<QAAnalysisResult?>(null) }
    var showOverlay by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    
    // The specific Figma link from the mail
    val figmaLink = "https://www.figma.com/proto/31OHtN6hkWaNKkAJ4YGIxA/User-profile---Settings-screen--Community-?node-id=11-2368"
    
    val analyzer = remember { IntentAnalyzer(BuildConfig.GEMINI_API_KEY) }
    val figmaService = remember { FigmaService(BuildConfig.FIGMA_TOKEN) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    ),
                    title = { Text("Settings", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                    actions = {
                        IconButton(
                            onClick = {
                                isAnalyzing = true
                                scope.launch {
                                    // 1. Capture Current Implementation
                                    val rootView = (context as ComponentActivity).window.decorView.rootView
                                    val screenshot = CaptureUtils.captureView(rootView)

                                    // 2. Parse Figma URL & Fetch Design Image
                                    val linkData = FigmaUrlParser.parse(figmaLink)
                                    var figmaDesign = if (linkData != null && linkData.nodeId != null) {
                                        figmaService.fetchComponentImage(linkData.fileKey, linkData.nodeId)
                                    } else null

                                    // Level 2 Fallback: Use local resource if live fetch failed
                                    if (figmaDesign == null) {
                                        try {
                                            val resId = context.resources.getIdentifier("figma_reference", "drawable", context.packageName)
                                            if (resId != 0) {
                                                figmaDesign = BitmapFactory.decodeResource(context.resources, resId)
                                            }
                                        } catch (e: Exception) { /* Ignore */ }
                                    }

                                    // 3. Perform AI Analysis (The Master Fallback Flow)
                                    val intent = VisualIntent(
                                        componentId = "Settings Screen",
                                        figmaNodeId = linkData?.nodeId ?: "",
                                        description = "Expert Android Settings screen with rounded gray cards and specific icons.",
                                        criticalElements = listOf("Background #F3F3F3", "Iconography", "Padding")
                                    )

                                    try {
                                        // Debug: Log if the API Key is dummy
                                        if (BuildConfig.GEMINI_API_KEY.contains("your_gemini")) {
                                            android.util.Log.e("PixelPerfect", "WARNING: Using dummy API Key!")
                                        }

                                        // Attempt Real AI Analysis
                                        val result = analyzer.analyze(screenshot, figmaDesign, intent)
                                        
                                        if (result.status != AnalysisStatus.UNCERTAIN) {
                                            analysisResult = result.copy(
                                                implementationImage = screenshot,
                                                designImage = figmaDesign
                                            )
                                        } else {
                                            // Show the specific AI failure in the result for debugging
                                            analysisResult = result.copy(
                                                feedback = "AI was Uncertain: ${result.feedback}\n\nFalling back to simulated report:\n\n" + getDummyResult(screenshot).feedback
                                            )
                                        }
                                    } catch (e: Exception) {
                                        android.util.Log.e("PixelPerfect", "AI Analysis Error", e)
                                        analysisResult = QAAnalysisResult(
                                            status = AnalysisStatus.UNCERTAIN,
                                            confidence = 0f,
                                            feedback = "AI ERROR: ${e.message}\n\nPlease check your GEMINI_API_KEY in local.properties and ensure the device has internet.",
                                            implementationImage = screenshot
                                        )
                                    }

                                    isAnalyzing = false
                                    showOverlay = true
                                }
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .background(Color(0xFFF0F0F5), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Scan",
                                tint = Color(0xFF1C1B1F)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color.Gray,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat")
                        
                        // Centered FAB-like button
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF1A1A40),
                            modifier = Modifier.size(56.dp).offset(y = (-10).dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                            }
                        }
                        
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFF1A1A40))
                        Icon(Icons.Default.PersonOutline, contentDescription = "Profile")
                    }
                }
            }
        ) { innerPadding ->
            SettingsScreen(modifier = Modifier.padding(innerPadding))
        }

        // Loading Overlay matching Screenshot 2
        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // Laser Scan Animation
                val infiniteTransition = rememberInfiniteTransition(label = "laser")
                val yOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "yOffset"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scanLineY = size.height * yOffset
                    drawLine(
                        color = Color(0xFFB066FF),
                        start = androidx.compose.ui.geometry.Offset(0f, scanLineY),
                        end = androidx.compose.ui.geometry.Offset(size.width, scanLineY),
                        strokeWidth = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                        alpha = 0.8f
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fetching Figma design and analyzing\nwith AI...",
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (showOverlay && analysisResult != null) {
            VisualQAOverlay(
                result = analysisResult!!,
                onDismiss = { showOverlay = false }
            )
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SettingsSection(title = "Account") {
            SettingsItem(icon = Icons.Default.PersonOutline, label = "Edit profile")
            SettingsItem(icon = Icons.Default.Security, label = "Security")
            SettingsItem(icon = Icons.Default.NotificationsNone, label = "Notifications")
            SettingsItem(icon = Icons.Default.LockOpen, label = "Privacy")
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Support & About") {
            SettingsItem(icon = Icons.Default.CreditCard, label = "My Subscription")
            SettingsItem(icon = Icons.AutoMirrored.Filled.Help, label = "Help & Support")
            SettingsItem(icon = Icons.Default.Info, label = "Terms and Policies")
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection(title = "Cache & cellular") {
            SettingsItem(icon = Icons.Default.DeleteOutline, label = "Free up space")
            SettingsItem(icon = Icons.Default.DataUsage, label = "Data Saver")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Actions", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(12.dp))
                .padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Black)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black)
    }
}

private fun getDummyResult(screenshot: Bitmap): QAAnalysisResult {
    return QAAnalysisResult(
        status = AnalysisStatus.VISUAL_REGRESSION,
        confidence = 0.95f,
        implementationImage = screenshot,
        feedback = """
            As an expert Android QA engineer, I have thoroughly compared the provided Implementation screenshot with the Figma Design reference. Below is a detailed breakdown of discrepancies and perfectly implemented sections, focusing on actionable feedback for a developer.
            
            ---
            ### Implementation vs. Design: QA Findings
            
            **Overall Status:** The Implementation shows significant deviations from the Design, particularly in the navigation bar, overall layout, and several iconography choices. The "Actions" section is entirely missing.
            
            ---
            ### Detailed Discrepancies:
            
            1. **Top Status Bar & Navigation Bar:**
               * **Discrepancy:** The Implementation entirely lacks the standard Android status bar and the designed "Settings" navigation bar (back button, title).
               * **Actionable Fix:** Implement a standard Android status bar. Implement a navigation bar with a left-aligned back arrow, a centered "Settings" title (using Roboto Medium 18sp), and no right-aligned icon.
               * **Discrepancy:** The Implementation displays an isolated "eye" icon on the top right. This icon is not present in the Design.
               * **Actionable Fix:** Remove the "eye" icon.
            
            2. **"Account" Section:**
               * **Layout/Background:**
                 * **Discrepancy:** The Implementation lacks the light gray, rounded-rectangle background card (#F3F3F3) specified in the Design.
                 * **Actionable Fix:** Wrap list items in a Card with background #F3F3F3 and 12dp corner radius.
               * **Typography:**
                 * **Discrepancy:** "Edit profile" in the Implementation uses lowercase 'p', whereas the Design specifies "Edit Profile" (Sentence case).
                 * **Actionable Fix:** Correct the casing to "Edit Profile".
        """.trimIndent(),
        suggestions = listOf("Remove eye icon", "Add back button", "Fix 'Edit Profile' casing")
    )
}
