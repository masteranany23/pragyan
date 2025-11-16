package com.example.pragyan


import androidx.compose.material3.ExperimentalMaterial3Api
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pragyan.ui.components.ControlButtons
import com.example.pragyan.ui.screens.VideoStreamScreen
import com.example.pragyan.ui.theme.PragyanTheme
import com.example.pragyan.viewmodel.RobotViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.pragyan.ui.screens.MainScreen
import com.example.pragyan.ui.screens.ModeScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PragyanTheme {
                RobotControlApp()
             //ModeScreen()
            // }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RobotControlApp(viewModel: RobotViewModel = hiltViewModel()) {

    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Navigation()
        }
    }
}}