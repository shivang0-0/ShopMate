package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.R

@Composable
fun AppFooter(isInStoreMode: Boolean, onNavigationItemClick: (Screen) -> Unit, activeScreen: Screen) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem(screen = Screen.Discover, onNavigationItemClick = onNavigationItemClick, activeScreen = activeScreen)
            NavigationItem(screen = Screen.Categories, onNavigationItemClick = onNavigationItemClick, activeScreen = activeScreen)

            if (isInStoreMode) {
                NavigationItem(screen = Screen.Map, onNavigationItemClick = onNavigationItemClick, activeScreen = activeScreen)
            }

            NavigationItem(screen = Screen.Wishlist, onNavigationItemClick = onNavigationItemClick, activeScreen = activeScreen)
            NavigationItem(screen = Screen.Profile, onNavigationItemClick = onNavigationItemClick, activeScreen = activeScreen)
        }
    }
}

@Composable
fun NavigationItem(screen: Screen, onNavigationItemClick: (Screen) -> Unit, activeScreen: Screen) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onNavigationItemClick(screen) },
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = screen.iconResId),
                contentDescription = screen.label,
                tint = if (screen == activeScreen) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

sealed class Screen(val iconResId: Int, val label: String) {
    object Discover : Screen(R.drawable.ic_discover, "Discover")
    object Categories : Screen(R.drawable.ic_category, "Categories")
    object Map : Screen(R.drawable.ic_map, "Map")
    object Wishlist : Screen(R.drawable.ic_favourite, "Wishlist")
    object Profile : Screen(R.drawable.ic_profile, "Profile")
    object Cart : Screen(R.drawable.ic_cart, "Cart")
}
