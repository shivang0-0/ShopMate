package com.sparkathon.shopmate.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun InStoreScreen(onNavigate: (Boolean) -> Unit) {
    // UI for In-Store Mode Home
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to In-Store Mode")
        // Simulate navigating away from the homepage
        Button(onClick = { onNavigate(false) }) {
            Text("Go to In-Store Details")
        }
    }
}
