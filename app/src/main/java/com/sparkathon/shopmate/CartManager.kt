package com.sparkathon.shopmate

import com.sparkathon.shopmate.Product

object CartManager {
    // List to hold cart items
    private val cartItems = mutableListOf<Product>()

    // Add a product to the cart
    fun addProduct(product: Product) {
        cartItems.add(product)
    }

    // Retrieve the list of items in the cart
    fun getCartItems(): List<Product> {
        return cartItems
    }

    // Clear the cart
    fun clearCart() {
        cartItems.clear()
    }

    // Remove a product from the cart (if needed)
    fun removeProduct(product: Product) {
        cartItems.remove(product)
    }
}
