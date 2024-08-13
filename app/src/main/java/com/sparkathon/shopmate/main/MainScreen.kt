package com.sparkathon.shopmate.main

import AppHeader
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sparkathon.shopmate.main.screens.InStoreScreen
import com.sparkathon.shopmate.main.screens.OnlineScreen

@Composable
fun MainScreen() {
    // State to track the selected mode and current page
    var isInStoreMode by remember { mutableStateOf(false) }
    var isHomePage by remember { mutableStateOf(true) }

    // Main Screen layout
    Column(modifier = Modifier.fillMaxSize()) {
        // Header with the app name, mode switch button, and cart option
        AppHeader(
            isInStoreMode = isInStoreMode,
            onModeSwitch = { isInStoreMode = !isInStoreMode },
            isHomePage = isHomePage,
            onCartClick = { /* Handle cart click */ }
        )

        // Content based on the selected mode
        Box(modifier = Modifier.weight(1f)) {
            if (isInStoreMode) {
                InStoreScreen(onNavigate = { isHomePage = it })
            } else {
                OnlineScreen(onNavigate = { isHomePage = it })
            }
        }

        // Footer with different items based on the mode
        AppFooter(
            isInStoreMode = isInStoreMode,
            onNavigationItemClick = { page ->
                when (page) {
                    Screen.Categories -> TODO()
                    Screen.Discover -> TODO()
                    Screen.Map -> TODO()
                    Screen.Profile -> TODO()
                    Screen.Wishlist -> TODO()
                }
            }
        )
    }
}
