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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.main.components.ProductItem
import com.sparkathon.shopmate.preferences.getCartItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun InStoreCartScreen() {
    val context = LocalContext.current
    val cartItems = getCartItems(context)
    var products by remember { mutableStateOf<List<Product>?>(null) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch the products from the API
    LaunchedEffect(cartItems) {
        val productIds = cartItems.keys.mapNotNull { it.toIntOrNull() }
        if (productIds.isNotEmpty()) {
            for (ProductId in productIds) {
                RetrofitInstance.api.getProducts(null, ProductId).enqueue(object : Callback<List<Product>> {
                    override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                        if (response.isSuccessful) {
                            products = products?.plus(response.body()!!) ?: response.body()
                        } else {
                            Log.e("InStoreCartScreen", "Failed to fetch products: ${response.errorBody()}")
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                        Log.e("InStoreCartScreen", "Error fetching products", t)
                        isLoading = false
                    }
                })
            }
        } else {
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
            // Show a loading indicator while fetching data
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (selectedProduct != null) {
                // Show the product detail view if a product is selected
                ProductViewScreen(product = selectedProduct!!)
            } else {
                products?.let { productList ->
                    if (productList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                        ) {
                            items(productList) { product ->
                                val quantity = cartItems[product.id.toString()] ?: 1
                                ProductItem(
                                    product = product,
                                    quantity = quantity,
                                    onProductClick = { product ->
                                        selectedProduct = product // Set the selected product
                                    })
                            }
                        }
                    } else {
                        // Show a message if the cart is empty
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Your cart is empty")
                        }
                    }
                } ?: run {
                    // Show a message if the cart is empty
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Your cart is empty")
                    }
                }
            }
        }
    }
}
