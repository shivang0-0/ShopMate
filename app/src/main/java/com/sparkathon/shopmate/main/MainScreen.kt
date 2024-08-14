package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sparkathon.shopmate.main.screens.*

@Composable
fun MainScreen() {
    var isInStoreMode by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Discover) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            isInStoreMode = isInStoreMode,
            onModeSwitch = { isInStoreMode = !isInStoreMode },
            onCartClick = { currentScreen = Screen.Cart },
            activeScreen = currentScreen
        )

        Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                Screen.Discover -> if (isInStoreMode) InStoreScreen() else OnlineScreen()
                Screen.Categories -> if (isInStoreMode) InStoreCategoriesScreen() else OnlineCategoriesScreen()
                Screen.Map -> InStoreMapScreen()
                Screen.Wishlist -> if (isInStoreMode) InStoreWishlistScreen() else OnlineWishlistScreen()
                Screen.Profile -> ProfileScreen()
                Screen.Cart -> if (isInStoreMode) InStoreCartScreen() else OnlineCartScreen()
            }
        }

        AppFooter(
            isInStoreMode = isInStoreMode,
            onNavigationItemClick = { screen ->
                currentScreen = screen
            },
            activeScreen = currentScreen
        )
    }
}
