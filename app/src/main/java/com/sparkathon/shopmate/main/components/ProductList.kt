package com.sparkathon.shopmate.main.components

import Product
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit, context: Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, onProductClick = onProductClick, context = context)
        }
    }
}

