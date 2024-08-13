package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sparkathon.shopmate.main.screens.InStoreScreen

@Composable
fun MainScreen() {
    // State to track the selected mode and current page
    var isInStoreMode by remember { mutableStateOf(false) }
    var isHomePage by remember { mutableStateOf(true) }

    // Main Screen layout
    Column(modifier = Modifier.fillMaxSize()) {
        // Header with the app name and mode switch button
        AppHeader(
            isInStoreMode = isInStoreMode,
            onModeSwitch = { isInStoreMode = !isInStoreMode },
            isHomePage = isHomePage
        )

        // Content based on the selected mode
        if (isInStoreMode) {
            InStoreScreen(onNavigate = { isHomePage = it })
        } else {
            OnlineScreen(onNavigate = { isHomePage = it })
        }
    }
}
