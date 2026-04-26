package com.example.budgetbasket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Locale

@Composable
fun DashboardScreen(
    userName: String,
    groupID: String,
    onOpenGroceryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val bannerColor = MaterialTheme.colorScheme.primaryContainer
    val bannerTextColor = MaterialTheme.colorScheme.onPrimaryContainer
    val subtitleColor = bannerTextColor.copy(alpha = 0.8f)
    val summaryTitleColor = Color(0xFF2E3A59)
    val summaryCardColor = Color(0xFFE3F2FD)

    val groceryButtonColor = Color(0xFF4CAF50)
    val logoutButtonColor = Color(0xFFFF7043)


    val repository = remember(groupID) { GroceryRepository(groupID) }
    var groceryItemCount by remember { mutableStateOf(0) }
    var totalExpenses by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        // use repository to get item count and total expenses
        repository.getItems { items, error ->
            if (error == null) {
                // update item count and total expenses
                groceryItemCount = items.size

                // update total expenses
                totalExpenses = items.sumOf { item ->
                    item.cost.toDoubleOrNull() ?: 0.0
                }
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
                containerColor = bannerColor
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
                    color = bannerTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Manage your shared groceries and expenses easily",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quick Summary",
                style = MaterialTheme.typography.titleLarge,
                color = summaryTitleColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenGroceryClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ITEMS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color(0xFF2E3A59).copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$groceryItemCount",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Tap to view all",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenGroceryClick() },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Expenses",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E3A59)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "€${String.format(Locale.getDefault(), "%.2f", totalExpenses)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Tap to view details",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Week View",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E3A59)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Weekly budget view coming soon",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = logoutButtonColor,
                contentColor = Color.White
            )
        ) {
            Text("Logout")
        }
    }
}