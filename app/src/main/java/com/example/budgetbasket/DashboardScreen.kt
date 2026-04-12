package com.example.budgetbasket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.*




import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun DashboardScreen(
    userName: String,
    onOpenGroceryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val backgroundColor = Color(0xFFF5F7FB)
    val bannerColor = Color(0xFF6C63FF)
    val bannerTextColor = Color.White
    val subtitleColor = Color(0xFFE8E7FF)
    val summaryTitleColor = Color(0xFF2E3A59)
    val summaryCardColor = Color(0xFFE3F2FD)

    val groceryButtonColor = Color(0xFF4CAF50)
    val logoutButtonColor = Color(0xFFFF7043)

    //Firestore and item count state
    val db = Firebase.firestore
    var groceryItemCount by remember {mutableStateOf(0)}

    //Total Expense
    var totalExpenses by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        db.collection("grocery_items")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener

                val documents = value?.documents ?: emptyList()

                groceryItemCount = documents.size

                totalExpenses = documents.sumOf { doc ->
                    doc.getString("cost")?.toDoubleOrNull() ?: 0.0
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xFF6C63FF)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "BudgetBasket",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Welcome, $userName!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Manage your shared groceries and expenses easily",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = summaryCardColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick Summary",
                    style = MaterialTheme.typography.titleLarge,
                    color = summaryTitleColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Your grocery items: $groceryItemCount", color = Color.Black)
                Text("Your expense total: €${String.format("%.2f", totalExpenses)}", color = Color.Black)
                Text("Weekly budget view: Coming soon", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onOpenGroceryClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = groceryButtonColor,
                contentColor = Color.White
            )
        ) {
            Text("Open Grocery List")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = logoutButtonColor,
                contentColor = Color.White
            )
        ) {
            Text("Logout")
        }
    }
}