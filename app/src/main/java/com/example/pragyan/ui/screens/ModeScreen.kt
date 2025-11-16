package com.example.pragyan.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pragyan.R
import com.example.pragyan.viewmodel.RobotViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ModeScreen(navController: NavController,viewModel: RobotViewModel = hiltViewModel(),modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Robot Mode",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedModeButton(
                    icon = R.drawable.photo_camera_24px,
                    text = "Object Detection",
                    color = Color(0xFF4285F4),
                    onClick = { viewModel.sendFeature("object_detection") }
                )
                AnimatedModeButton(
                    icon = R.drawable.timeline_24px,
                    text = "Line Following",
                    color = Color(0xFFEA4335),
                    onClick = { viewModel.sendFeature("line_following") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedModeButton(
                    icon = R.drawable.mic_24px,
                    text = "Voice Assistant",
                    color = Color(0xFFFBBC05),
                    onClick = { viewModel.sendFeature("maya")/* Handle click */ }
                )
                AnimatedModeButton(
                    icon = R.drawable.map_24px,
                    text = "Area Mapping",
                    color = Color(0xFF34A853),
                    onClick = { /* Handle click */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedModeButton(
                    icon = R.drawable.hand_gesture_24px,
                    text = "Attendance",
                    color = Color(0xFF9C27B0),
                    onClick = { viewModel.sendFeature("attend") }
                )
                AnimatedModeButton(
                    icon = R.drawable.ic_face_recognition,
                    text = "Face Recognition",
                    color = Color(0xFF00BCD4),
                    onClick = { /* Handle click */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 4
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedModeButton(
                    icon = R.drawable.ic_autonomous,
                    text = "Autonomous Mode",
                    color = Color(0xFFFF9800),
                    onClick = { /* Handle click */ }
                )
                AnimatedModeButton(
                    icon = R.drawable.ic_remote_control,
                    text = "Remote Control",
                    color = Color(0xFF3F51B5),
                    onClick = { /* Handle click */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedModeButton(
                    icon = R.drawable.ic_obstacle_avoid,
                    text = "Obstacle Avoidance",
                    color = Color(0xFFFF5722),
                    onClick = { /* Handle click */ }
                )
                AnimatedModeButton(
                    icon = R.drawable.ic_learn_mode,
                    text = "Learning Mode",
                    color = Color(0xFF607D8B),
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}

@Composable
fun AnimatedModeButton(
    icon: Int,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    // Animation state
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Scale animation
    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    // Elevation animation
    val elevation by animateDpAsState(
        targetValue = if (isClicked) 2.dp else 6.dp,
        animationSpec = tween(durationMillis = 100),
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(130.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Button(
            onClick = {
                // Trigger animation and then execute onClick
                coroutineScope.launch {
                    isClicked = true
                    delay(100) // Short delay for animation
                    onClick()
                    delay(100) // Keep animation visible briefly
                    isClicked = false
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun ModeScreenPreview() {
//    MaterialTheme {
//        ModeScreen()
//    }
//}