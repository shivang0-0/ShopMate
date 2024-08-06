package com.sparkathon.shopmate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryRecyclerView = findViewById(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

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
