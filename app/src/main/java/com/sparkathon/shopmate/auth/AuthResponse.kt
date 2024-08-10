package com.sparkathon.shopmate.auth

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)
