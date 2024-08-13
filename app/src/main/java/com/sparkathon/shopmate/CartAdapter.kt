package com.sparkathon.shopmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class CartAdapter(
    private val cartItems: MutableList<Product>,
    private val onCartUpdated: () -> Unit // Callback to update total amount
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.bind(product)
    }

    override fun getItemCount() = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)
        private val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        private val btnIncreaseQuantity: Button = itemView.findViewById(R.id.btn_increase_quantity)
        private val btnDecreaseQuantity: Button = itemView.findViewById(R.id.btn_decrease_quantity)
        private val btnRemoveItem: Button = itemView.findViewById(R.id.btn_remove_item)

        fun bind(product: Product) {
            productName.text = product.title
            productPrice.text = "₹${product.price * product.quantity}"
            productQuantity.text = product.quantity.toString()

            // Increase quantity
            btnIncreaseQuantity.setOnClickListener {
                product.quantity++
                notifyItemChanged(adapterPosition)
                onCartUpdated() // Update total amount
            }

            // Decrease quantity
            btnDecreaseQuantity.setOnClickListener {
                if (product.quantity > 1) {
                    product.quantity--
                    notifyItemChanged(adapterPosition)
                    onCartUpdated() // Update total amount
                }
            }

            // Remove item
            btnRemoveItem.setOnClickListener {
                cartItems.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                onCartUpdated() // Update total amount
            }
        }
    }
}
