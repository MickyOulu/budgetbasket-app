package com.example.budgetbasket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

                var currentUserName by remember { mutableStateOf("")
                }

                var currentGroupId by remember { mutableStateOf("") }

                LaunchedEffect(firebaseUser) {
                    val userId = firebaseUser?.uid

                    if (userId != null) {
                        db.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                currentUserName = document.getString("name") ?: ""
                                currentGroupId = document.getString("groupId") ?: ""
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
                            onLoginSuccess = {
                                val loggedInUser = auth.currentUser
                                val userId = loggedInUser?.uid

                                if (userId != null) {
                                    db.collection("users")
                                        .document(userId)
                                        .get()
                                        .addOnSuccessListener { document ->
                                            currentUserName = document.getString("name") ?: (loggedInUser.email ?: "")
                                            currentGroupId = document.getString("groupId") ?: ""
                                            currentScreen = "dashboard"
                                        }
                                } else {
                                    currentScreen = "dashboard"
                                }
                            }
                        )

                        "signup" -> SignUpScreen(
                            onBackToLoginClick = { currentScreen = "login" },
                            onSignUpSuccess = { enteredName ->
                                currentUserName = enteredName
                                currentScreen = "dashboard"
                            }
                        )

                        "dashboard" -> {
                            // if currentGroupId is empty, show a loading screen
                            if (currentGroupId.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                DashboardScreen(
                                    userName = currentUserName,
                                    groupID = currentGroupId,
                                    onOpenGroceryClick = { currentScreen = "grocery" },
                                    onLogoutClick = {
                                        auth.signOut()
                                        currentUserName = ""
                                        currentGroupId = ""
                                        currentScreen = "login"
                                    }
                                )
                            }
                        }

                        "grocery" -> GroceryListScreen(
                            currentUserName = currentUserName,
                            groupID = currentGroupId,
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