package com.sparkathon.shopmate.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OnlineScreen(onNavigate: (Boolean) -> Unit) {
    // UI for Online Mode Home
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to Online Mode")
        // Simulate navigating away from the homepage
        Button(onClick = { onNavigate(false) }) {
            Text("Go to Online Details")
        }
    }
}
