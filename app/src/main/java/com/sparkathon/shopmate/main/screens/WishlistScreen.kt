package com.sparkathon.shopmate.main.screens

import Product
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.main.components.ProductItem
import com.sparkathon.shopmate.preferences.getWishlistItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun WishlistScreen() {
    val context = LocalContext.current
    val productIds by remember { mutableStateOf(getWishlistItems(context)) }
    var products by remember { mutableStateOf<List<Product>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productIds) {
        if (productIds.isNotEmpty()) {
            for (productId in productIds) {
                RetrofitInstance.api.getProducts(null, productId).enqueue(object : Callback<List<Product>> {
                    override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                        if (response.isSuccessful) {
                            products = products?.plus(response.body()!!) ?: response.body()
                        } else {
                            Log.e("OnlineCartScreen", "Failed to fetch products: ${response.errorBody()}")
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                        Log.e("OnlineCartScreen", "Error fetching products", t)
                        isLoading = false
                    }
                })
            }
        } else {
            products = emptyList()
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (selectedProduct != null) {
                ProductViewScreen(product = selectedProduct!!)
            } else {
                if (products.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Your wishlist is empty")
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                        ) {
                            items(products!!, key = { it.id }) { product ->
                                ProductItem(
                                    product = product,
                                    onProductClick = { selectedProduct = product },
                                    context = context
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
