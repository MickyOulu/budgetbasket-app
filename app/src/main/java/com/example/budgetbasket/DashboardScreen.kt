package com.example.budgetbasket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    userName: String,
    onOpenGroceryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome, $userName",
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Quick Summary", color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Your grocery items: Coming soon", color = Color.Black)
                Text("Your expense total: Coming soon", color = Color.Black)
                Text("Weekly budget view: Coming soon", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onOpenGroceryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Grocery List")
        }
    }
}