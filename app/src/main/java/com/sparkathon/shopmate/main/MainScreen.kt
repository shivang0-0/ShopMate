package com.sparkathon.shopmate.main

import AppHeader
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sparkathon.shopmate.main.screens.InStoreScreen
import com.sparkathon.shopmate.main.screens.OnlineScreen

@Composable
fun MainScreen() {
    var isInStoreMode by remember { mutableStateOf(false) }
    var isHomePage by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            isInStoreMode = isInStoreMode,
            onModeSwitch = { isInStoreMode = !isInStoreMode },
            isHomePage = isHomePage,
            onCartClick = { TODO() }
        )

        Box(modifier = Modifier.weight(1f)) {
            if (isInStoreMode) {
                InStoreScreen(onNavigate = { isHomePage = it })
            } else {
                OnlineScreen(onNavigate = { isHomePage = it })
            }
        }

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
