package com.sparkathon.shopmate.main.components

import Product
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit, context: Context) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp), // Adaptive grid with minimum item width
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalItemSpacing = 2.dp,  // Space between items vertically
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between items horizontally
    ) {
        items(products.size) { index ->
            val product = products[index]
            ProductItem(product = product, onProductClick = onProductClick, context = context)
        }
    }
}
