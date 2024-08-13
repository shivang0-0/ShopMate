package com.sparkathon.shopmate.auth

import LoginRequest
import RegisterRequest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.main.MainActivity
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import androidx.compose.runtime.LaunchedEffect
import java.util.concurrent.TimeUnit

class AuthActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ShopMatePrefs", MODE_PRIVATE)

        setContent {
            ShopMateTheme {
                AuthScreen()
            }
        }
    }

    @Composable
    fun AuthScreen() {
        var showLoginForm by remember { mutableStateOf(true) }

        // Check token validity on launch
        LaunchedEffect(Unit) {
            if (isTokenValid()) {
                navigateToMainScreen()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ShopMate",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (showLoginForm) {
                    LoginForm(onLogin = { email, password -> login(email, password) })
                } else {
                    RegisterForm(onRegister = { email, password, firstname, lastname, mobile ->
                        register(email, password, firstname, lastname, mobile)
                    })
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { showLoginForm = !showLoginForm }) {
                    Text(if (showLoginForm) "Continue with Register" else "Continue with Login")
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        val api = RetrofitInstance.api
        val request = LoginRequest(email, password)
        val call = api.login(request)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    saveToken(response.body()?.token)
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
                    saveToken(response.body()?.token)
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

    private fun saveToken(token: String?) {
        token?.let {
            val editor = sharedPreferences.edit()
            editor.putString("token", it)
            // Assuming the token expires in 1 hour
            val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
            editor.putLong("expiryTime", expiryTime)
            editor.apply()
        }
    }

    private fun isTokenValid(): Boolean {
        val token = sharedPreferences.getString("token", null)
        val expiryTime = sharedPreferences.getLong("expiryTime", 0)

        if (token.isNullOrEmpty() || System.currentTimeMillis() > expiryTime) {
            return false
        }
        return true
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
