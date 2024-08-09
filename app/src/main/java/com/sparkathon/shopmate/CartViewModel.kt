package com.sparkathon.shopmate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<Product>>()
    val cartItems: LiveData<List<Product>> = _cartItems

    init {
        _cartItems.value = mutableListOf()
    }

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentItems.add(product)
        _cartItems.value = currentItems

        // Print the cart items to the console
        Log.d("CartViewModel", "Cart Items:")
        currentItems.forEach { item ->
            Log.d("CartViewModel", "ID: ${item.id}, Title: ${item.title}, Price: ${item.price}, Image: ${item.image}")
        }

    }

    fun removeFromCart(product: Product) {
        val currentItems = _cartItems.value?.toMutableList()
        currentItems?.remove(product)
        _cartItems.value = currentItems
    }
}
