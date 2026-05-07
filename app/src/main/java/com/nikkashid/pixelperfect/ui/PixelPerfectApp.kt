package com.nikkashid.pixelperfect.ui

import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.qa.CaptureUtils
import com.nikkashid.pixelperfect.qa.VisualQAOverlay
import com.nikkashid.pixelperfect.ui.settings.SettingsScreen
import com.nikkashid.pixelperfect.ui.settings.SettingsUiState
import com.nikkashid.pixelperfect.ui.settings.SettingsViewModel
import com.nikkashid.pixelperfect.ui.theme.LaserPurple
import com.nikkashid.pixelperfect.ui.theme.PrimaryBrandDark
import com.nikkashid.pixelperfect.ui.theme.ScanButtonBackground
import com.nikkashid.pixelperfect.ui.theme.ScanIconTint
import com.nikkashid.pixelperfect.ui.theme.UIConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelPerfectApp(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    ),
                    title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                    actions = {
                        IconButton(
                            onClick = {
                                val rootView = (context as ComponentActivity).window.decorView.rootView
                                val screenshot = CaptureUtils.captureView(rootView)
                                viewModel.performScan(screenshot, context)
                            },
                            modifier = Modifier
                                .padding(end = UIConstants.SmallSpacing)
                                .background(ScanButtonBackground, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = stringResource(R.string.scan_content_description),
                                tint = ScanIconTint
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color.Gray,
                    tonalElevation = UIConstants.BottomBarElevation
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home_content_description))
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = stringResource(R.string.chat_content_description))
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBrandDark,
                            modifier = Modifier.size(UIConstants.FabSize).offset(y = UIConstants.FabVerticalOffset)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_content_description), tint = Color.White)
                            }
                        }
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings_title), tint = PrimaryBrandDark)
                        Icon(Icons.Default.PersonOutline, contentDescription = stringResource(R.string.profile_content_description))
                    }
                }
            }
        ) { innerPadding ->
            SettingsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }

        // Handle UI States
        when (val state = uiState) {
            is SettingsUiState.Loading -> LoadingOverlay()
            is SettingsUiState.Success -> {
                VisualQAOverlay(
                    result = state.result,
                    onDismiss = { viewModel.dismissOverlay() }
                )
            }
            is SettingsUiState.Error -> {
                LaunchedEffect(state) {
                    Log.e("PixelPerfect", "Error: ${state.message}")
                }
            }
            SettingsUiState.Idle -> Unit
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = UIConstants.BackgroundOverlayAlpha)),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "laser")
        val yOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(UIConstants.LaserScanDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "yOffset"
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val scanLineY = size.height * yOffset
            drawLine(
                color = LaserPurple,
                start = androidx.compose.ui.geometry.Offset(0f, scanLineY),
                end = androidx.compose.ui.geometry.Offset(size.width, scanLineY),
                strokeWidth = UIConstants.LaserLineWidth.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                alpha = UIConstants.LaserLineAlpha
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(UIConstants.MediumSpacing))
            Text(
                text = stringResource(R.string.fetching_ai_message),
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
