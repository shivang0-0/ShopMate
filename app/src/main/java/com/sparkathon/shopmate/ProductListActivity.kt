package com.sparkathon.shopmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.util.Log

class ProductListActivity : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        productRecyclerView = findViewById(R.id.product_recycler_view)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        val category = intent.getStringExtra("CATEGORY") ?: return

        // Debugging log to ensure category is correctly passed
        Log.d("ProductListActivity", "Selected category: $category")

        RetrofitInstance.api.getProductsByCategory(category).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    productRecyclerView.adapter = ProductAdapter(products ?: emptyList())
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(this@ProductListActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@ProductListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

