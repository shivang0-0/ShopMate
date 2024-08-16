package com.sparkathon.shopmate.main.screens

import Profile
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkathon.shopmate.api.RetrofitInstance
import com.sparkathon.shopmate.auth.AuthActivity
import com.sparkathon.shopmate.preferences.getEmailFromPreferences
import com.sparkathon.shopmate.ui.theme.ShopMateTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import showToast

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val email = getEmailFromPreferences(context) ?: return

    var userProfile by remember { mutableStateOf(Profile(email, "", "", "")) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(email) {
        Log.d("ProfileScreen", "Fetching profile for email: $email")
        getProfile(context, email) { profile ->
            if (profile.isNotEmpty()) {
                userProfile = profile[0]
                Log.d("ProfileScreen", "Profile fetched: $userProfile")
            } else {
                Log.d("ProfileScreen", "No profiles found.")
            }
            isLoading = false
        }
    }

    ShopMateTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 6.dp
                )
            } else {
                ProfileCard(userProfile = userProfile)
            }
        }
    }
}

@Composable
fun ProfileCard(userProfile: Profile) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = userProfile.firstname.firstOrNull()?.toString()?.uppercase() ?: "",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${userProfile.firstname} ${userProfile.lastname}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Email: ${userProfile.email}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mobile: ${userProfile.mobile}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { signOut(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}

private fun signOut(context: Context) {
    val sharedPreferences = context.getSharedPreferences("ShopMatePrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("token")
    editor.remove("expiryTime")
    editor.apply()

    showToast(context, "Signed out successfully")

    val intent = Intent(context, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

private fun getProfile(context: Context, email: String, onResult: (List<Profile>) -> Unit) {
    val api = RetrofitInstance.api
    val call = api.getProfile(email)

    call.enqueue(object : Callback<List<Profile>> {
        override fun onResponse(call: Call<List<Profile>>, response: Response<List<Profile>>) {
            if (response.isSuccessful) {
                val profiles = response.body()
                if (!profiles.isNullOrEmpty()) {
                    Log.d("ProfileScreen", "Profiles retrieved successfully: $profiles")
                    onResult(profiles)
                } else {
                    showToast(context, "No profiles found")
                    Log.d("ProfileScreen", "No profiles found in the response.")
                    onResult(emptyList())
                }
            } else {
                showToast(context, "Failed to fetch profiles: ${response.message()}")
                Log.d("ProfileScreen", "Failed to fetch profiles: ${response.message()}")
                onResult(emptyList())
            }
        }

        override fun onFailure(call: Call<List<Profile>>, t: Throwable) {
            t.printStackTrace()
            showToast(context, "Failed to fetch profiles: ${t.message}")
            Log.d("ProfileScreen", "Error fetching profiles: ${t.message}")
            onResult(emptyList())
        }
    })
}
