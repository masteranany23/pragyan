package com.example.pragyan.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoStreamScreen(
    piIp: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                // Use a WebViewClient to handle page navigation
                webViewClient = WebViewClient()
                // Load the stream URL from the Raspberry Pi
                loadUrl("http://192.168.184.48/")
            }
        },
        modifier = modifier
    )
}
