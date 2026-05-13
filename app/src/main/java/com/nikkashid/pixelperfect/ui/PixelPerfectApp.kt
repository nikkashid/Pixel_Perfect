package com.nikkashid.pixelperfect.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.qa.CaptureUtils
import com.nikkashid.pixelperfect.qa.VisualQAOverlay
import com.nikkashid.pixelperfect.ui.profile.EditProfileScreen
import com.nikkashid.pixelperfect.ui.settings.PixelPerfectScreen
import com.nikkashid.pixelperfect.ui.settings.SettingsScreen
import com.nikkashid.pixelperfect.ui.settings.SettingsUiState
import com.nikkashid.pixelperfect.ui.settings.SettingsViewModel
import com.nikkashid.pixelperfect.ui.theme.PrimaryBrandDark
import com.nikkashid.pixelperfect.ui.theme.ScanButtonBackground
import com.nikkashid.pixelperfect.ui.theme.ScanIconTint
import com.nikkashid.pixelperfect.ui.theme.UIConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelPerfectApp(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    ),
                    title = { 
                        val title = when(currentScreen) {
                            PixelPerfectScreen.SETTINGS -> stringResource(R.string.settings_title)
                            PixelPerfectScreen.EDIT_PROFILE -> stringResource(R.string.edit_profile_title)
                        }
                        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp) 
                    },
                    navigationIcon = {
                        if (currentScreen == PixelPerfectScreen.EDIT_PROFILE) {
                            IconButton(onClick = { viewModel.navigateTo(PixelPerfectScreen.SETTINGS) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
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
                // Bottom bar only shows on Settings screen per Figma design
                if (currentScreen == PixelPerfectScreen.SETTINGS) {
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
                            IconButton(onClick = { /* Home */ }) {
                                Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home_content_description))
                            }
                            IconButton(onClick = { /* Chat */ }) {
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = stringResource(R.string.chat_content_description))
                            }
                            Surface(
                                shape = CircleShape,
                                color = PrimaryBrandDark,
                                modifier = Modifier.size(UIConstants.FabSize).offset(y = UIConstants.FabVerticalOffset)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_content_description), tint = Color.White)
                                }
                            }
                            IconButton(onClick = { viewModel.navigateTo(PixelPerfectScreen.SETTINGS) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings, 
                                    contentDescription = stringResource(R.string.settings_title), 
                                    tint = PrimaryBrandDark // bar only shows when on Settings
                                )
                            }
                            IconButton(onClick = { viewModel.navigateTo(PixelPerfectScreen.EDIT_PROFILE) }) {
                                Icon(
                                    imageVector = Icons.Default.PersonOutline, 
                                    contentDescription = stringResource(R.string.profile_content_description),
                                    tint = Color.Gray // bar only shows when NOT on Profile
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentScreen) {
                    PixelPerfectScreen.SETTINGS -> SettingsScreen(viewModel)
                    PixelPerfectScreen.EDIT_PROFILE -> EditProfileScreen()
                }
            }
        }

        // Handle UI States via MVVM
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
                    android.util.Log.e("PixelPerfect", "Error: ${state.message}")
                }
            }
            SettingsUiState.Idle -> { /* Do nothing */ }
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
