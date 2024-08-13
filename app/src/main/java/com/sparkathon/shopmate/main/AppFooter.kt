package com.sparkathon.shopmate.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkathon.shopmate.R

@Composable
fun AppFooter(isInStoreMode: Boolean, onNavigationItemClick: (Screen) -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.secondary, // Footer background color
        contentColor = MaterialTheme.colorScheme.onSecondary // Icon and text color
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround, // Distribute items evenly
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem(screen = Screen.Discover, onNavigationItemClick = onNavigationItemClick)
            NavigationItem(screen = Screen.Categories, onNavigationItemClick = onNavigationItemClick)

            if (isInStoreMode) {
                NavigationItem(screen = Screen.Map, onNavigationItemClick = onNavigationItemClick)
            }

            NavigationItem(screen = Screen.Wishlist, onNavigationItemClick = onNavigationItemClick)
            NavigationItem(screen = Screen.Profile, onNavigationItemClick = onNavigationItemClick)
        }
    }
}

@Composable
fun NavigationItem(screen: Screen, onNavigationItemClick: (Screen) -> Unit) {
    Column(
        modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { onNavigationItemClick(screen) }) {
            Icon(
                painter = painterResource(id = screen.iconResId),
                contentDescription = screen.label,
                tint = MaterialTheme.colorScheme.onPrimary
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
}
