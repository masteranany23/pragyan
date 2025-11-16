package com.example.pragyan.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pragyan.R
import com.example.pragyan.viewmodel.RobotViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: RobotViewModel = hiltViewModel()) {
    val context = LocalContext.current

    // Collect the current IP from the ViewModel's StateFlow
    val currentIpAddress by viewModel.currentIpAddress.collectAsState()

    // Allow manual override if needed
    var useManualIp by remember { mutableStateOf(false) }
    var manualIpInput by remember { mutableStateOf(currentIpAddress) }
    var isStreaming by remember { mutableStateOf(false) }

    // Update manual IP input when the current IP changes and we're not in manual mode
    LaunchedEffect(currentIpAddress) {
        if (!useManualIp) {
            manualIpInput = currentIpAddress
        }
    }

    // Effect to update the IP address in the view model when manual settings change
    LaunchedEffect(useManualIp, manualIpInput) {
        if (useManualIp) {
            viewModel.setManualIp(true, manualIpInput)
        } else {
            viewModel.setManualIp(false)
        }
    }

    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
    ) {
        // IP Input Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (useManualIp) {
                    OutlinedTextField(
                        value = manualIpInput,
                        onValueChange = { manualIpInput = it },
                        label = { Text("Raspberry Pi IP (Manual)") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    OutlinedTextField(
                        value = currentIpAddress,
                        onValueChange = { /* Read only */ },
                        label = { Text("Raspberry Pi IP (Auto-detected)") },
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = { isStreaming = !isStreaming },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isStreaming) Color.Red else Color.Green
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(if (isStreaming) "Stop Stream" else "Start Stream")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    useManualIp = !useManualIp
                    if (!useManualIp) {
                        viewModel.setManualIp(false)
                    }
                }) {
                    Text(if (useManualIp) "Use Auto IP" else "Enter IP Manually")
                }
            }
        }

        // Video Stream Section
        if (isStreaming) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
            ) {
                // Use the current IP for the video stream, whether manual or auto
                VideoStreamScreen(piIp = if (useManualIp) manualIpInput else currentIpAddress)
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Press Start to connect to ${if (useManualIp) manualIpInput else currentIpAddress}")
            }
        }

        // Navigation Controls
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Joystick-like Navigation
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Up Button
                NavigationButton(
                    iconRes = R.drawable.ic_arrow_up,
                    onClick = { viewModel.sendMovementCommand("F") },
                    modifier = Modifier.size(64.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left Button
                    NavigationButton(
                        iconRes = R.drawable.ic_arrow_left,
                        onClick = { viewModel.sendMovementCommand("L") },
                        modifier = Modifier.size(64.dp)
                    )

                    // Right Button
                    NavigationButton(
                        iconRes = R.drawable.ic_arrow_right,
                        onClick = { viewModel.sendMovementCommand("R") },
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Down Button
                NavigationButton(
                    iconRes = R.drawable.ic_arrow_down,
                    onClick = { viewModel.sendMovementCommand("B") },
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Additional Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ControlButton(
                    text = "Mode",
                    onClick = { navController.navigate("mode_screen") }
                )
                ControlButton(
                    text = "MAYA",
                    onClick = { viewModel.sendFeature("maya") }
                )
                ControlButton(
                    text = "Stop",
                    onClick = { viewModel.sendMovementCommand("stop_feature") }
                )
            }
        }
    }
}

@Composable
fun NavigationButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun ControlButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
    ) {
        Text(text = text)
    }
}