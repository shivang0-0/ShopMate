package com.sparkathon.shopmate.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sparkathon.shopmate.main.screens.*

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope


@Composable
fun MainScreen(nfcTagData: String?) {
    var isInStoreMode by rememberSaveable { mutableStateOf(false) }

    // Custom Saver for the navigation stack
    val navigationStackSaver = Saver<MutableList<Screen>, List<Screen>>(
        save = { it.toList() },
        restore = { it.toMutableStateList() }
    )

    // RememberSaveable with custom Saver for navigation stack
    val navigationStack = rememberSaveable(saver = navigationStackSaver) {
        mutableStateListOf<Screen>(Screen.Discover)
    }

    // Get the current screen from the stack
    val currentScreen = navigationStack.last()

    // Handle the back button press
    BackHandler {
        if (navigationStack.size > 1) {
            navigationStack.removeLast() // Go back to the previous screen
        } else {
            // Optionally handle exit logic
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            isInStoreMode = isInStoreMode,
            onModeSwitch = { isInStoreMode = !isInStoreMode },
            onCartClick = { navigationStack.add(Screen.Cart) },
            activeScreen = currentScreen
        )

        Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                is Screen.Discover -> if (isInStoreMode) InStoreExploreScreen() else OnlineScreenScreen()
                is Screen.Categories -> CategoriesScreen()
                is Screen.Map -> InStoreMapScreen(nfcTagData = nfcTagData)
                is Screen.Wishlist -> WishlistScreen()
                is Screen.Profile -> ProfileScreen()
                is Screen.Cart -> if (isInStoreMode) InStoreCartScreen() else OnlineCartScreen()
            }
        }

        AppFooter(
            isInStoreMode = isInStoreMode,
            onNavigationItemClick = { screen ->
                navigationStack.add(screen)
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
