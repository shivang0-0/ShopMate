package com.sparkathon.shopmate

import com.sparkathon.shopmate.Product
object CartManager {
    private val cartItems = mutableListOf<Product>()

    fun addProduct(product: Product) {
        val existingProduct = cartItems.find { it.id == product.id }
        if (existingProduct != null) {
            existingProduct.quantity += 1  // Increment the quantity
        } else {
            cartItems.add(product.copy(quantity = 1))  // Add new product with quantity 1
        }
    }

    fun getCartItems(): MutableList<Product> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }
}

