package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(isInStoreMode: Boolean, onModeSwitch: () -> Unit, isHomePage: Boolean) {
    // Header layout
    TopAppBar(
        title = { Text(text = "ShopMate") },
        actions = {
            if (isHomePage) {
                // Mode switch button only appears on homepage
                Button(onClick = onModeSwitch) {
                    Text(text = if (isInStoreMode) "Online" else "In-Store")
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
