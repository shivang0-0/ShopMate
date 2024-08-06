package com.sparkathon.shopmate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sparkathon.shopmate.databinding.ItemProductBinding

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.title
            binding.productPrice.text = product.price.toString()
            Picasso.get().load(product.image).into(binding.productImage)

            // Simulate varying item heights for the masonry effect
            val params = binding.productImage.layoutParams
            params.height = (300 + Math.random() * 300).toInt()  // random height between 300 and 600
            binding.productImage.layoutParams = params
        }
    }
}
