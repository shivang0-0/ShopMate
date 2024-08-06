import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.util.*

class UPIPayment(private val context: Context) {

    companion object {
        const val UPI_PAYMENT = 0
    }

    fun payUsingUPI(amount: String, upiId: String, name: String, note: String) {
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // Show the intent chooser
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        if (chooser.resolveActivity(context.packageManager) != null) {
            (context as Activity).startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(context, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleUPIPaymentResponse(requestCode: Int, resultCode: Int, data: Intent?, onResponse: (String) -> Unit) {
        if (requestCode == UPI_PAYMENT) {
            if (data != null) {
                val trxt = data.getStringExtra("response")
                when (resultCode) {
                    Activity.RESULT_OK, 11 -> {
                        if (trxt != null) {
                            onResponse(trxt)
                        } else {
                            onResponse("nothing")
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        onResponse("cancelled")
                    }
                }
            } else {
                onResponse("nothing")
            }
        }
    }

    fun processUPIPaymentResponse(response: String) {
        var paymentCancel = ""
        var status = ""
        var approvalRefNo = ""
        val responseArray = response.split("&".toRegex()).toTypedArray()
        for (res in responseArray) {
            val equalStr = res.split("=".toRegex()).toTypedArray()
            if (equalStr.size >= 2) {
                when (equalStr[0].lowercase(Locale.getDefault())) {
                    "status" -> {
                        status = equalStr[1].lowercase(Locale.getDefault())
                    }
                    "approvalrefno", "txnref" -> {
                        approvalRefNo = equalStr[1]
                    }
                }
            } else {
                paymentCancel = "Payment cancelled by user."
            }
        }
        when {
            status == "success" -> {
                Toast.makeText(context, "Transaction successful.", Toast.LENGTH_SHORT).show()
                // Handle successful transaction
            }
            "Payment cancelled by user." == paymentCancel -> {
                Toast.makeText(context, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
                // Handle payment cancellation
            }
            else -> {
                Toast.makeText(context, "Transaction failed. Please try again.", Toast.LENGTH_SHORT).show()
                // Handle failed transaction
            }
        }
    }
}
