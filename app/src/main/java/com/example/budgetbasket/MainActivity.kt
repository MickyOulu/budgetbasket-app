package com.example.budgetbasket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import com.example.budgetbasket.ui.theme.BudgetBasketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetBasketTheme {
                var showLoginScreen by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showLoginScreen) {
                        LoginScreen(modifier = Modifier.padding(innerPadding))
                    } else {
                        WelcomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            onGetStartedClick = { showLoginScreen = true }
                        )
                    }
                }
            }
        }
    }
}





