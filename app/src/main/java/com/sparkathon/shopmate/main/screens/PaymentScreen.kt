package com.sparkathon.shopmate.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparkathon.shopmate.preferences.clearCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun PaymentScreen() {
    var cardNumber by remember { mutableStateOf("") }
    var cardCvc by remember { mutableStateOf("") }
    var cardExpirationDate by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var showErrorAnimation by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val textFieldColors = MaterialTheme.colorScheme.secondary
    val context = LocalContext.current

    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val isCvcValid = cardCvc.length == 3 && cardCvc.all { it.isDigit() }
    val isExpirationDateValid = Regex("^(0[1-9]|1[0-2])/\\d{4}\$").matches(cardExpirationDate)
    val isCardNameValid = cardName.isNotBlank()

    val isFormValid = isCardNumberValid && isCvcValid && isExpirationDateValid && isCardNameValid

    if (showSuccessAnimation) {
        SuccessAnimation(onAnimationEnd = {
            showSuccessAnimation = false
            responseMessage = ""
        })
    } else if (showErrorAnimation) {
        ErrorAnimation(onAnimationEnd = {
            showErrorAnimation = false
            responseMessage = ""
        })
    } else {
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textFieldColors,
                    unfocusedTextColor = textFieldColors,
                    errorTextColor = textFieldColors,
                    cursorColor = textFieldColors,
                    focusedLabelColor = textFieldColors,
                    unfocusedLabelColor = textFieldColors,
                ),
                isError = !isCardNumberValid && cardNumber.isNotBlank()
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textFieldColors,
                    unfocusedTextColor = textFieldColors,
                    cursorColor = textFieldColors,
                    focusedLabelColor = textFieldColors,
                    unfocusedLabelColor = textFieldColors,
                    errorTextColor = textFieldColors
                ),
                isError = !isCvcValid && cardCvc.isNotBlank()
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textFieldColors,
                    unfocusedTextColor = textFieldColors,
                    cursorColor = textFieldColors,
                    focusedLabelColor = textFieldColors,
                    unfocusedLabelColor = textFieldColors,
                    errorTextColor = textFieldColors
                ),
                isError = !isExpirationDateValid && cardExpirationDate.isNotBlank()
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textFieldColors,
                    unfocusedTextColor = textFieldColors,
                    cursorColor = textFieldColors,
                    focusedLabelColor = textFieldColors,
                    unfocusedLabelColor = textFieldColors,
                    errorTextColor = textFieldColors
                ),
                isError = !isCardNameValid && cardName.isNotBlank()
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
                            if (response.contains("\"Payment Status\": \"Payment Received\"")){
                                showSuccessAnimation = true
                                clearCart(context)
                            } else {
                                showErrorAnimation = true
                                kotlinx.coroutines.delay(5000)
                                showErrorAnimation = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Submit Payment")
            }
        }
    }
}

@Composable
fun SuccessAnimation(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 0.5f
    )

    LaunchedEffect(progress) {
        if (progress == 1.0f) {
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(composition = composition, progress = { progress })
    }
}

@Composable
fun ErrorAnimation(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("error.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 0.3f
    )

    LaunchedEffect(progress) {
        if (progress == 1.0f) {
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(composition = composition, progress = { progress })
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
