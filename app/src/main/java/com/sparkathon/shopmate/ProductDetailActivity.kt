package com.sparkathon.shopmate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import android.widget.Toast


class ProductDetailActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Retrieve the product details from the intent
        val productId = intent.getIntExtra("PRODUCT_ID", 0)
        val productTitle = intent.getStringExtra("PRODUCT_TITLE") ?: ""
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: ""

        val product = Product(productId, productTitle, productPrice, productImage)
//        Log.d("CartViewModel", "Cart Items: $currentItems")
        // Find the views in the layout
        val titleTextView: TextView = findViewById(R.id.productDetailTitle)
        val priceTextView: TextView = findViewById(R.id.productDetailPrice)
        val imageView: ImageView = findViewById(R.id.productDetailImage)
        val addToCartButton: Button = findViewById(R.id.addToCartButton)

        // Set the views with the product details
        titleTextView.text = product.title
        priceTextView.text = getString(R.string.product_price, product.price)
        Picasso.get().load(product.image).into(imageView)

        // Handle "Add to Cart" button click
        addToCartButton.setOnClickListener {
            cartViewModel.addToCart(product)
            // Optionally, show a confirmation message
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        }
    }
}
