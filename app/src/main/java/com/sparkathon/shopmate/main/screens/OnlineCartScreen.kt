package com.sparkathon.shopmate.main.screens

import Product
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.sparkathon.shopmate.preferences.getCartItems
import com.sparkathon.shopmate.preferences.removeProductFromCart
import com.sparkathon.shopmate.preferences.updateProductQuantity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun OnlineCartScreen() {
    val context = LocalContext.current
    var cartItems by remember { mutableStateOf(getCartItems(context)) }
    var products by remember { mutableStateOf<List<Product>?>(null) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("cart") }

    LaunchedEffect(cartItems) {
        val productIds = cartItems.keys.mapNotNull { it.toIntOrNull() }
        if (productIds.isNotEmpty()) {
            products = emptyList()
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

    if (currentScreen == "payment") {
        PaymentScreen()
    } else {
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
                            Text(text = "Your cart is empty")
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
                                    var quantity by remember { mutableStateOf(cartItems[product.id.toString()] ?: 1) }

                                    ProductItem(
                                        product = product,
                                        onProductClick = { selectedProduct = product }
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                quantity += 1
                                                updateProductQuantity(context, product.id.toString(), quantity)
                                            }
                                        ) {
                                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase Quantity")
                                        }
                                        Text(text = "$quantity")
                                        IconButton(
                                            onClick = {
                                                if (quantity > 1) {
                                                    quantity -= 1
                                                    updateProductQuantity(context, product.id.toString(), quantity)
                                                } else {
                                                    removeProductFromCart(context, product.id.toString())
                                                    cartItems = getCartItems(context)
                                                    products = products?.filterNot { it.id == product.id }
                                                    if (products.isNullOrEmpty()) {
                                                        products = emptyList()
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease Quantity")
                                        }
                                        IconButton(
                                            onClick = {
                                                removeProductFromCart(context, product.id.toString())
                                                cartItems = getCartItems(context)
                                                products = products?.filterNot { it.id == product.id }
                                                if (products.isNullOrEmpty()) {
                                                    products = emptyList()
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Filled.Delete, contentDescription = "Remove Product")
                                        }
                                    }
                                }
                            }

                            // Calculate total price
                            val totalPrice = products?.sumOf { product ->
                                val quantity = cartItems[product.id.toString()] ?: 1
                                product.price * quantity
                            } ?: 0.0

                            // Display total price and checkout button
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Total: $${"%.2f".format(totalPrice)}",
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Button(
                                    onClick = { currentScreen = "payment" },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Text(text = "Checkout")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
