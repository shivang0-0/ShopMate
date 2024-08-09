package com.sparkathon.shopmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val btnOnline = findViewById<Button>(R.id.btn_online)
        val btnStore = findViewById<Button>(R.id.btn_store)

        btnOnline.setOnClickListener {
            // Redirect to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnStore.setOnClickListener {
            // Redirect to StoreActivity
            startActivity(Intent(this, MainActivityInstore::class.java))
        }
    }
}
