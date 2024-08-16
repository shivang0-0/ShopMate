package com.sparkathon.shopmate.main.components

import Product
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sparkathon.shopmate.preferences.addToWishlist
import com.sparkathon.shopmate.preferences.getWishlistItems
import com.sparkathon.shopmate.preferences.removeFromWishlist

@Composable
fun ProductItem(product: Product, onProductClick: (Product) -> Unit, context: Context) {
    val wishlist = getWishlistItems(context)
    var isWished by remember { mutableStateOf(wishlist.contains(product.id)) }

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f) // Adjust aspect ratio to fit the image
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = if (isWished) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Wishlist Icon",
                    tint = if (isWished) Color.Red else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (isWished) {
                                removeFromWishlist(context, product.id)
                            } else {
                                addToWishlist(context, product.id)
                            }
                            Log.d("ProductItem", "isWished: $isWished; product.id: ${product.id}")
                            isWished = !isWished
                        }
                )
            }
        }
    }
}
