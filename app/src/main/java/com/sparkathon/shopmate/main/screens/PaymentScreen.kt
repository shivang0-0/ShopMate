package com.sparkathon.shopmate.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen() {
    var cardNumber by remember { mutableStateOf("") }
    var cardCvc by remember { mutableStateOf("") }
    var cardExpirationDate by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val textFieldColors = MaterialTheme.colorScheme.secondary

    // Validation checks
    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val isCvcValid = cardCvc.length == 3 && cardCvc.all { it.isDigit() }
    val isExpirationDateValid = Regex("^(0[1-9]|1[0-2])/\\d{4}\$").matches(cardExpirationDate)
    val isCardNameValid = cardName.isNotBlank()

    // Form validation
    val isFormValid = isCardNumberValid && isCvcValid && isExpirationDateValid && isCardNameValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number (16 digits)") },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = textFieldColors,
                unfocusedTextColor = textFieldColors,
                cursorColor = textFieldColors,
                focusedLabelColor = textFieldColors,
                unfocusedLabelColor = textFieldColors,
                errorTextColor = textFieldColors
            ),
            isError = cardNumber.isBlank()
        )
        if (!isCardNumberValid && cardNumber.isNotBlank()) {
            Text(
                text = "Invalid card number. Must be 16 digits.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = cardCvc,
            onValueChange = { cardCvc = it },
            label = { Text("CVC (3 digits)") },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = textFieldColors,
                unfocusedTextColor = textFieldColors,
                cursorColor = textFieldColors,
                focusedLabelColor = textFieldColors,
                unfocusedLabelColor = textFieldColors,
                errorTextColor = textFieldColors
            ),
            isError = cardNumber.isBlank()
        )
        if (!isCvcValid && cardCvc.isNotBlank()) {
            Text(
                text = "Invalid CVC. Must be 3 digits.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = cardExpirationDate,
            onValueChange = { cardExpirationDate = it },
            label = { Text("Expiration Date (MM/YYYY)") },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = textFieldColors,
                unfocusedTextColor = textFieldColors,
                cursorColor = textFieldColors,
                focusedLabelColor = textFieldColors,
                unfocusedLabelColor = textFieldColors,
                errorTextColor = textFieldColors
            ),
            isError = cardNumber.isBlank()
        )
        if (!isExpirationDateValid && cardExpirationDate.isNotBlank()) {
            Text(
                text = "Invalid expiration date. Must be MM/YYYY.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = cardName,
            onValueChange = { cardName = it },
            label = { Text("Cardholder Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = textFieldColors,
                unfocusedTextColor = textFieldColors,
                cursorColor = textFieldColors,
                focusedLabelColor = textFieldColors,
                unfocusedLabelColor = textFieldColors,
                errorTextColor = textFieldColors
            ),
            isError = cardNumber.isBlank()
        )
        if (!isCardNameValid && cardName.isNotBlank()) {
            Text(
                text = "Cardholder name cannot be empty.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if (isFormValid) {
                    coroutineScope.launch {
                        val response = sendPaymentRequest(
                            cardNumber,
                            cardCvc,
                            cardExpirationDate,
                            cardName
                        )
                        responseMessage = response
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text("Submit Payment")
        }

        if (responseMessage.isNotEmpty()) {
            Text(
                text = responseMessage,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall
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
        connection.requestMethod = "POST" // Use POST for sending data securely
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
