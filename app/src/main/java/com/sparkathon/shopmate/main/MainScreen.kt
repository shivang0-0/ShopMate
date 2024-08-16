package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sparkathon.shopmate.main.screens.*

@Composable
fun MainScreen(nfcTagData: String?) {
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
                Screen.Discover -> if (isInStoreMode) InStoreExploreScreen() else OnlineScreenScreen()
                Screen.Categories -> CategoriesScreen()
                Screen.Map -> InStoreMapScreen(nfcTagData = nfcTagData)
                Screen.Wishlist -> WishlistScreen()
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

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(nfcTagData = null) // Provide a default value for preview
}
