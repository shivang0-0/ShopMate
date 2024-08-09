package com.sparkathon.shopmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Initialize RecyclerView
        categoryRecyclerView = findViewById(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize BottomNavigationView
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_discover -> {
                    startActivity(Intent(this, DiscoverActivity::class.java))
                    true
                }
                R.id.nav_category -> {
                    // Do nothing since we're already in this activity
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
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
        bottomNav.selectedItemId = R.id.nav_category

        // Load categories
        RetrofitInstance.api.getCategories().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    if (categories != null) {
                        categoryRecyclerView.adapter = CategoryAdapter(categories) { category ->
                            val intent = Intent(this@CategoryActivity, ProductListActivity::class.java)
                            intent.putExtra("CATEGORY", category)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
