package com.example.budgetbasket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.budgetbasket.ui.theme.BudgetBasketTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BudgetBasketTheme {
                val auth = FirebaseAuth.getInstance()
                val firebaseUser = auth.currentUser

                val db = FirebaseFirestore.getInstance()

                var currentScreen by remember {
                    mutableStateOf(
                        if (firebaseUser != null) "dashboard" else "welcome"
                    )
                }

                var currentUserName by remember {
                    mutableStateOf("")
                }

                LaunchedEffect(firebaseUser) {
                    val userId = firebaseUser?.uid

                    if (userId != null) {
                        db.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                val savedName = document.getString("name")
                                if (!savedName.isNullOrEmpty()) {
                                    currentUserName = savedName
                                }
                            }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    when (currentScreen) {
                        "welcome" -> WelcomeScreen(
                            onGetStartedClick = { currentScreen = "login" }
                        )

                        "login" -> LoginScreen(
                            onSignUpClick = { currentScreen = "signup" },
                            onLoginSuccess = { enteredName ->
                                currentUserName = enteredName
                                currentScreen = "dashboard"
                            }
                        )

                        "signup" -> SignUpScreen(
                            onBackToLoginClick = { currentScreen = "login" },
                            onSignUpSuccess = { enteredName ->
                                currentUserName = enteredName
                                currentScreen = "dashboard"
                            }
                        )

                        "dashboard" -> DashboardScreen(
                            userName = currentUserName,
                            onOpenGroceryClick = { currentScreen = "grocery" },
                            onLogoutClick = {
                                auth.signOut()
                                currentUserName = ""
                                currentScreen = "login"
                            }
                        )

                        "grocery" -> GroceryListScreen(
                            currentUserName = currentUserName,
                            onBackClick = { currentScreen = "dashboard" }
                        )

                        else -> WelcomeScreen(
                            onGetStartedClick = { currentScreen = "login" }
                        )
                    }
                }
            }
        }
    }
}