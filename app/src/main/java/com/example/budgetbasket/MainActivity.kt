package com.example.budgetbasket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetbasket.ui.theme.BudgetBasketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetBasketTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding)
                   )

                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var showSecondScreen by remember { mutableStateOf(false) }

    if (showSecondScreen){
        SecondScreen()
    } else {
        //Home
    }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Text(message)
        Text(
            text = "BudgetBasket",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Manage shared grocery shopping and expenses",
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Button(onClick = { showSecondScreen= true }) {
            Text(text = "Get Started")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BudgetBasketTheme {
        HomeScreen()
    }
}

@Composable
fun SecondScreen(){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is the next Screen!")
    }
}
