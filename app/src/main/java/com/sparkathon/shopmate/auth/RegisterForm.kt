package com.sparkathon.shopmate.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(onRegister: (String, String, String, String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text("Mobile") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onRegister(email, password, firstname, lastname, mobile) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Register")
            }
        }
    }
}
