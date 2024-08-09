package com.sparkathon.shopmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize RecyclerView
        val cartRecyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Observe cart items and update the adapter
        cartViewModel.cartItems.observe(this) { cartItems ->
            cartRecyclerView.adapter = ProductAdapter(cartItems)
        }

        // Initialize BottomNavigationView
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_discover -> {
                    startActivity(Intent(this, DiscoverActivity::class.java))
                    true
                }
                R.id.nav_category -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    // Do nothing since we're already in this activity
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the selected item in BottomNavigationView
        bottomNav.selectedItemId = R.id.nav_cart
    }
}
