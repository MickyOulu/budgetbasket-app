package com.example.budgetbasket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var resetMessage by remember { mutableStateOf("")}

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome back to BudgetBasket",
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                message = ""
            },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email Icon")
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                message = ""
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password Icon")
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    email.isBlank() && password.isBlank() -> {
                        message = "Please enter email and password"
                    }
                    email.isBlank() -> {
                        message = "Please enter your email"
                    }
                    password.isBlank() -> {
                        message = "Please enter your password"
                    }
                    else -> {
                        auth.signInWithEmailAndPassword(email.trim(), password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userEmail = auth.currentUser?.email ?: "User"
                                    onLoginSuccess(userEmail)
                                } else {
                                    message = task.exception?.localizedMessage ?: "Login failed"
                                }
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                modifier = Modifier.padding(top = 12.dp),
                color = Color.Red
            )
        }


        Text(
            text = "Forgot Password?",
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
                .clickable(onClick = {
                    if (email.isBlank()) {
                        message = "Enter your email first"
                        resetMessage = ""
                    } else {
                        auth.sendPasswordResetEmail(email.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    resetMessage = "Reset email sent"
                                    message = ""
                                } else {
                                    resetMessage =
                                        task.exception?.localizedMessage ?: "Error occurred"
                                }
                            }
                    }
                })
        )
        if (resetMessage.isNotEmpty()){
            Text(
                text = resetMessage,
                color = Color.Red,
                modifier = Modifier.padding(top=8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSignUpClick) {
            Text("Go to Sign Up")
        }
    }
}