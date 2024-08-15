package com.sparkathon.shopmate.main.screens

import Category
import Product
import Rating
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import showToast

@Composable
fun CategoriesScreen() {
    ShopMateTheme {
        var currentScreen by remember { mutableStateOf("categories") }
        var selectedCategory by remember { mutableStateOf("") }
        var selectedProduct by remember { mutableStateOf(Product(0, "", 0.0, "", 0, "", "", Rating(0.0f, 0))) }
        val context = LocalContext.current
        var categories by remember { mutableStateOf(emptyList<String>()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            RetrofitInstance.api.getCategories().enqueue(object : Callback<List<Category>> {
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    Log.d("CategoriesScreen", "Response received: ${response.body()}")
                    if (response.isSuccessful) {
                        categories = response.body()?.map { it.name } ?: emptyList()
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    showToast(context, "Failed to fetch categories: ${t.message}")
                    Log.e("CategoriesScreen", "Failed to fetch categories", t)
                    isLoading = false
                }
            })
        }

        if (currentScreen == "categories") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    CategoryList(categories = categories) { category ->
                        selectedCategory = category
                        currentScreen = "productCategoryView"
                    }
                }
            }
        } else if (currentScreen == "productCategoryView") {
            ProductCategoryViewScreen(category = selectedCategory, onProductClick = { product ->
                selectedProduct = product
                currentScreen = "productView"
            })
        } else if (currentScreen == "productView") {
            ProductViewScreen(product = selectedProduct)
        }
    }
}

@Composable
fun CategoryList(categories: List<String>, onCategoryClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onCategoryClick = onCategoryClick)
        }
    }
}

@Composable
fun CategoryItem(category: String, onCategoryClick: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCategoryClick(category) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}