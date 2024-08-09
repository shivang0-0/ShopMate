package com.sparkathon.shopmate

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Retrieve the product details from the intent
        val productTitle = intent.getStringExtra("PRODUCT_TITLE")
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productImage = intent.getStringExtra("PRODUCT_IMAGE")

        // Find the views in the layout
        val titleTextView: TextView = findViewById(R.id.productDetailTitle)
        val priceTextView: TextView = findViewById(R.id.productDetailPrice)
        val imageView: ImageView = findViewById(R.id.productDetailImage)

        // Set the views with the product details
        titleTextView.text = productTitle
        priceTextView.text = getString(R.string.product_price, productPrice)
        Picasso.get().load(productImage).into(imageView)
    }
}
