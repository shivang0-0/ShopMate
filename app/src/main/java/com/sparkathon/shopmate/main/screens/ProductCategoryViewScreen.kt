package com.sparkathon.shopmate.main.screens

import Product
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.main.components.ProductList
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import showToast

@Composable
fun ProductCategoryViewScreen(category: String, onProductClick: (Product) -> Unit) {
    ShopMateTheme {
        val context = LocalContext.current
        var products by remember { mutableStateOf(emptyList<Product>()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(category) {
            RetrofitInstance.api.getProducts(category, id = null).enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    Log.d("ProductCategoryViewScreen", "Response received: ${response.body()}")
                    if (response.isSuccessful) {
                        products = response.body() ?: emptyList()
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    showToast(context, "Failed to fetch products: ${t.message}")
                    Log.e("ProductCategoryViewScreen", "Failed to fetch products", t)
                    isLoading = false
                }
            })
        }

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
                ProductList(products = products) { product ->
                    onProductClick(product)
                }
            }
        }
    }
}