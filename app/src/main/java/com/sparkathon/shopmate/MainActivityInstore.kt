package com.sparkathon.shopmate

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivityInstore : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalAmountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_instore)

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalAmountTextView = findViewById(R.id.total_amount)
        cartAdapter = CartAdapter(CartManager.getCartItems()) { updateTotalAmount() }
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize total amount
        updateTotalAmount()
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val tag: Tag? = it.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let { nfcTag ->
                val ndef = Ndef.get(nfcTag)
                ndef?.connect()
                val ndefMessage = ndef?.ndefMessage
                val payload = ndefMessage?.records?.get(0)?.payload
                val productId = String(payload ?: ByteArray(0)).trim()

                // Add the product to the cart
                addToCart(productId)
                ndef?.close()
            }
        }
    }

    private fun addToCart(productId: String) {
        try {
            // Remove non-printable characters, including control characters
            val cleanProductId = productId.filter { it.isLetterOrDigit() }
            Log.d("MainActivityInstore", "Clean product ID: $cleanProductId")

            // Remove the "en" prefix and log the result
            val idString = cleanProductId.removePrefix("en")
            Log.d("MainActivityInstore", "Product ID after removing prefix: $idString")

            // Convert the remaining part to an integer
            val id = idString.toInt()
            Log.d("MainActivityInstore", "Converted product ID: $id")

            val retrofitInstance = RetrofitInstance.api

            retrofitInstance.getProductById(id).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        val product = response.body()

                        product?.let {
                            CartManager.addProduct(it)
                            cartAdapter.notifyDataSetChanged() // Notify adapter of changes
                            updateTotalAmount() // Update total amount
                            Toast.makeText(this@MainActivityInstore, "${it.title} added to cart.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivityInstore, "Failed to fetch product details.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Log.e("MainActivityInstore", "Error fetching product details: ${t.message}")
                    Toast.makeText(this@MainActivityInstore, "Error adding product to cart.", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: NumberFormatException) {
            Log.e("MainActivityInstore", "Invalid product ID after cleaning: $productId", e)
            Toast.makeText(this, "Invalid product ID: $productId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("MainActivityInstore", "Unexpected error: ${e.message}", e)
            Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotalAmount() {
        val totalAmount = CartManager.getCartItems().sumOf { it.price * it.quantity }
        totalAmountTextView.text = "Total: ₹$totalAmount"
    }
}
