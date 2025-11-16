package com.example.pragyan.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward


import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pragyan.viewmodel.RobotViewModel

@Composable
fun ControlButtons(
    viewModel: RobotViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.horizontalScroll(scrollState )
    ) {
        // Forward
        Button(
            onClick = { viewModel.sendCommand("F") },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, "Forward")
        }
        // Backward
        Button(
            onClick = { viewModel.sendCommand("B") },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, "Backward")
        }

        // Left
        Button(
            onClick = { viewModel.sendCommand("L") },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Left")
        }



        // Right
        Button(
            onClick = { viewModel.sendCommand("R") },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Right")
        }
        // Stop
        Button(
            onClick = { viewModel.sendCommand("S") },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(Icons.Default.Close, "Stop")
        }


    }
}