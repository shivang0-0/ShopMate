package com.sparkathon.shopmate.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sparkathon.shopmate.main.MainActivity
import com.sparkathon.shopmate.databinding.ActivityAuthBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            login(email, password)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            register(email, password)
        }
    }

    private fun login(email: String, password: String) {
        // Assume you have a Retrofit API instance for login
        val api = RetrofitInstance.api
        val call = api.login(email, password)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    navigateToMainScreen()
                } else {
                    showToast("Login failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun register(email: String, password: String) {
        // Assume you have a Retrofit API instance for registration
        val api = RetrofitInstance.api
        val call = api.register(email, password)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    showToast("Registration successful")
                } else {
                    showToast("Registration failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
