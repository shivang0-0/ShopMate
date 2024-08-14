package com.sparkathon.shopmate.api

import LoginRequest
import Product
import ProductRequest
import ProductbyCategoryRequest
import ProductbyIDRequest
import RegisterRequest
import com.sparkathon.shopmate.auth.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @GET("products")
    fun getProducts(@Body request: ProductRequest): Call<List<Product>>

    @GET("products/categories")
    fun getProductsByCategory(@Body request: ProductbyCategoryRequest): Call<List<Product>>

    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("products")
    fun getProductById(@Body request: ProductbyIDRequest): Call<Product>
}
