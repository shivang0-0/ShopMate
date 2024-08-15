package com.sparkathon.shopmate.payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymentScreen()
        }
    }
}

@Composable
fun PaymentScreen() {
    var cardNumber by remember { mutableStateOf("") }
    var cardCvc by remember { mutableStateOf("") }
    var cardExpirationDate by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cardCvc,
            onValueChange = { cardCvc = it },
            label = { Text("CVC") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cardExpirationDate,
            onValueChange = { cardExpirationDate = it },
            label = { Text("Expiration Date (MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cardName,
            onValueChange = { cardName = it },
            label = { Text("Cardholder Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    val response = sendPaymentRequest(
                        cardNumber,
                        cardCvc,
                        cardExpirationDate,
                        cardName
                    )
                    responseMessage = response
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Payment")
        }
        if (responseMessage.isNotEmpty()) {
            Text(
                text = responseMessage,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

suspend fun sendPaymentRequest(
    cardNumber: String,
    cardCvc: String,
    cardExpirationDate: String,
    cardName: String
): String {
    return withContext(Dispatchers.IO) {
        val url = URL("http://143.110.182.65:6969/")
        val jsonBody = JSONObject().apply {
            put("card_number", cardNumber.toLong())
            put("card_cvc", cardCvc.toInt())
            put("card_expiration_date", cardExpirationDate)
            put("card_name", cardName)
        }

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        connection.outputStream.use { outputStream ->
            outputStream.write(jsonBody.toString().toByteArray())
        }

        return@withContext connection.inputStream.use { inputStream ->
            inputStream.bufferedReader().readText()
        }
    }
}
