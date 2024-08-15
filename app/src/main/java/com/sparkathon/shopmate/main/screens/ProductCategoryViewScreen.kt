package com.sparkathon.shopmate.main.screens

import Product
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.sparkathon.shopmate.api.RetrofitInstance
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

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, onProductClick = onProductClick)
        }
    }
}

@Composable
fun ProductItem(product: Product, onProductClick: (Product) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onProductClick(product) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Price: $${product.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}