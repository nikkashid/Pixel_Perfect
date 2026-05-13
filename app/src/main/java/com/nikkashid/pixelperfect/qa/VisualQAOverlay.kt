package com.nikkashid.pixelperfect.qa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.data.QAAnalysisResult
import com.nikkashid.pixelperfect.ui.theme.CardBackground
import com.nikkashid.pixelperfect.ui.theme.LaserPurple
import com.nikkashid.pixelperfect.ui.theme.LightDivider
import com.nikkashid.pixelperfect.ui.theme.OverlayBackdrop
import com.nikkashid.pixelperfect.ui.theme.UIConstants

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
            color = OverlayBackdrop
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(UIConstants.HeaderPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = LaserPurple,
                            modifier = Modifier.size(UIConstants.LargeIconSize)
                        )
                        Spacer(modifier = Modifier.width(UIConstants.SmallSpacing))
                        Text(
                            text = stringResource(R.string.ai_analysis_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = UIConstants.TitleFontSize,
                            color = Color.Black
                        )
                    }
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        shape = RoundedCornerShape(UIConstants.LargeSpacing),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = UIConstants.DialogElevation)
                    ) {
                        Text(stringResource(R.string.done_button), fontWeight = FontWeight.SemiBold)
                    }
                }

                // Small vertical implementation preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = UIConstants.LargeSpacing),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.current_implementation_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = UIConstants.SmallSpacing)
                        )
                        Box(
                            modifier = Modifier
                                .width(UIConstants.PreviewWidth)
                                .height(UIConstants.PreviewHeight)
                                .clip(RoundedCornerShape(UIConstants.SmallSpacing))
                                .background(Color.White)
                                .padding(UIConstants.TinySpacing)
                        ) {
                            result.implementationImage?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = stringResource(R.string.current_implementation_label),
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            } ?: Box(
                                modifier = Modifier.fillMaxSize().background(Color(0xFFE5E5EA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.no_preview_available), color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }
                }

                // Findings Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UIConstants.MediumSpacing, vertical = UIConstants.SmallSpacing),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(UIConstants.LargeCornerRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = UIConstants.CardElevation)
                ) {
                    Column(modifier = Modifier.padding(UIConstants.LargeSpacing)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.findings_header),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            // Display Data Source Badge
                            Surface(
                                color = LaserPurple.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(UIConstants.TinySpacing)
                            ) {
                                Text(
                                    text = result.dataSource,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LaserPurple
                                )
                            }
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = UIConstants.MediumSpacing),
                            thickness = UIConstants.DividerThickness,
                            color = LightDivider
                        )
                        
                        Text(
                            text = result.feedback,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = UIConstants.BodyLineHeight,
                            color = Color.DarkGray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(UIConstants.ExtraLargeSpacing))
            }
        }
    }
}
