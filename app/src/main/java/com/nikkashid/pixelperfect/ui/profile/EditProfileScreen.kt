package com.nikkashid.pixelperfect.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikkashid.pixelperfect.R
import com.nikkashid.pixelperfect.ui.theme.UIConstants
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Scrollable Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = UIConstants.MediumSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier.padding(top = UIConstants.LargeSpacing, bottom = UIConstants.LargeSpacing),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F3F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MP",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF5A5A9E),
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .border(2.dp, Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(UIConstants.SmallSpacing))

            // Input Fields
            ProfileInput(label = stringResource(R.string.label_name), value = stringResource(R.string.value_name))
            ProfileInput(label = stringResource(R.string.label_email), value = stringResource(R.string.value_email))
            ProfileInput(label = stringResource(R.string.label_password), value = stringResource(R.string.value_password))
            ProfileInput(label = stringResource(R.string.label_dob), value = stringResource(R.string.value_dob), isDropdown = true)
            ProfileInput(label = stringResource(R.string.label_country), value = stringResource(R.string.value_country), isDropdown = true)
            
            // Spacer to ensure content doesn't hide behind the button
            Spacer(modifier = Modifier.height(140.dp))
        }

        // Pinned "Save changes" Button - Narrow width and fixed bottom gap
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 48.dp, end = 48.dp, bottom = 32.dp, top = 8.dp) // Narrowed button to match Figma
        ) {
            Button(
                onClick = { /* Save Changes */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(UIConstants.SmallSpacing),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262650))
            ) {
                Text(
                    text = stringResource(R.string.save_changes_button), 
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInput(
    label: String,
    value: String,
    isDropdown: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = UIConstants.SmallSpacing)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        
        BasicTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            readOnly = true,
            singleLine = true,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    trailingIcon = {
                        if (isDropdown) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(UIConstants.SmallSpacing),
                            focusedBorderThickness = 1.dp,
                            unfocusedBorderThickness = 1.dp
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                )
            }
        )
    }
}
