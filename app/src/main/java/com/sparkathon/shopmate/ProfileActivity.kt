package com.sparkathon.shopmate

import UPIPayment
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var upiPayment: UPIPayment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        upiPayment = UPIPayment(this)

        upiPayment.payUsingUPI("100", "siddharthakatiyar25-1@okicici", "Siddhartha Katiyar", "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        upiPayment.handleUPIPaymentResponse(requestCode, resultCode, data) { response ->
            upiPayment.processUPIPaymentResponse(response)
        }
    }
}
