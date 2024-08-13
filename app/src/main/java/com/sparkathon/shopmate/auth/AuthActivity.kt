package com.sparkathon.shopmate.auth

import LoginRequest
import RegisterRequest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sparkathon.shopmate.api.RetrofitInstance
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
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.registerPasswordEditText.text.toString().trim()
            val firstname = binding.firstnameEditText.text.toString().trim()
            val lastname = binding.lastnameEditText.text.toString().trim()
            val mobile = binding.mobileEditText.text.toString().trim()
            register(email, password, firstname, lastname, mobile)
        }

        binding.switchToRegisterButton.setOnClickListener {
            toggleForms(showLoginForm = false)
        }

        binding.switchToLoginButton.setOnClickListener {
            toggleForms(showLoginForm = true)
        }
    }

    private fun login(email: String, password: String) {
        val api = RetrofitInstance.api
        val request = LoginRequest(email, password)
        val call = api.login(request)

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

    private fun register(email: String, password: String, firstname: String, lastname: String, mobile: String) {
        val api = RetrofitInstance.api
        val request = RegisterRequest(email, password, firstname, lastname, mobile)
        val call = api.register(request)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    showToast("Registration successful")
                    navigateToMainScreen()
                } else {
                    showToast("Registration failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun toggleForms(showLoginForm: Boolean) {
        binding.loginForm.visibility = if (showLoginForm) View.VISIBLE else View.GONE
        binding.registerForm.visibility = if (showLoginForm) View.GONE else View.VISIBLE
        binding.switchToRegisterButton.visibility = if (showLoginForm) View.VISIBLE else View.GONE
        binding.switchToLoginButton.visibility = if (showLoginForm) View.GONE else View.VISIBLE
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
