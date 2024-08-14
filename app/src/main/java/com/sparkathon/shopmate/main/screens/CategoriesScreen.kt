package com.sparkathon.shopmate.main.screens

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun OnlineCategoriesScreen() {
    ShopMateTheme {
        val context = LocalContext.current
        var categories by remember { mutableStateOf(emptyList<String>()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            // Load categories
            RetrofitInstance.api.getCategories().enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        categories = response.body() ?: emptyList()
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    // Handle failure
                    isLoading = false
                }
            })
        }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    onNavItemClicked = { route ->
                        when (route) {
                            "discover" -> context.startActivity(Intent(context, DiscoverActivity::class.java))
                            "category" -> Unit // No action needed as we're already here
                            "favorite" -> context.startActivity(Intent(context, FavoriteActivity::class.java))
                            "cart" -> context.startActivity(Intent(context, CartActivity::class.java))
                            "profile" -> context.startActivity(Intent(context, ProfileActivity::class.java))
                        }
                    },
                    selectedItem = "category"
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    CategoryList(categories = categories) { category ->
                        val intent = Intent(context, ProductListActivity::class.java)
                        intent.putExtra("CATEGORY", category)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryList(categories: List<String>, onCategoryClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onCategoryClick = onCategoryClick)
        }
    }
}

@Composable
fun CategoryItem(category: String, onCategoryClick: (String) -> Unit) {
    Text(
        text = category,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCategoryClick(category) }
            .padding(vertical = 8.dp)
    )
}

@Composable
fun BottomNavigationBar(onNavItemClicked: (String) -> Unit, selectedItem: String) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ) {
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_discover), contentDescription = "Discover") },
            label = { Text("Discover") },
            selected = selectedItem == "discover",
            onClick = { onNavItemClicked("discover") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_category), contentDescription = "Categories") },
            label = { Text("Categories") },
            selected = selectedItem == "category",
            onClick = { onNavItemClicked("category") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_favorite), contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = selectedItem == "favorite",
            onClick = { onNavItemClicked("favorite") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_cart), contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = selectedItem == "cart",
            onClick = { onNavItemClicked("cart") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(R.drawable.ic_profile), contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == "profile",
            onClick = { onNavItemClicked("profile") }
        )
    }
}