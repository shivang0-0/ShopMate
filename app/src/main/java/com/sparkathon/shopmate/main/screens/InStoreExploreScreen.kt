package com.sparkathon.shopmate.main.screens

import Product
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.main.components.ProductList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import showToast

@Composable
fun InStoreExploreScreen() {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var products by remember { mutableStateOf(emptyList<Product>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                Log.d("CategoriesScreen", "Response received: ${response.body()}")
                if (response.isSuccessful) {
                    products = response.body() ?: emptyList()
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                showToast(context, "Failed to fetch categories: ${t.message}")
                Log.e("CategoriesScreen", "Failed to fetch categories", t)
                isLoading = false
            }
        })
    }

    if (selectedProduct != null) {
        ProductViewScreen(product = selectedProduct!!)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        )
        {
            ProductList(
                products = products,
                onProductClick = { product ->
                    selectedProduct = product
                },
                context = context
            )
        }
    }
}
