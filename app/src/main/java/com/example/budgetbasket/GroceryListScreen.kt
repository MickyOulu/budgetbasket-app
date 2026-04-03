package com.example.budgetbasket

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import android.window.BackEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


data class GroceryItem(
    val id: String = "",
    val itemName: String = "",
    val cost: String = "",
    val addedBy: String = "",
    val date: String = "",
    val week: String = ""
)

@Composable
fun GroceryListScreen(
    currentUserName: String,
    onBackClick: () -> Unit,
) {
    val db = Firebase.firestore
    var itemText by remember { mutableStateOf("") }
    var costText by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var weekText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val groceryItems = remember { mutableStateListOf<GroceryItem>() }

    val totalExpenses = groceryItems.sumOf { item ->
        item.cost.toDoubleOrNull() ?: 0.0
    }

    LaunchedEffect(Unit) {
        db.collection("grocery_items")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener

                // Clear the old list and add the new data from the cloud
                groceryItems.clear()
                for (doc in value!!.documents) {
                    val item = doc.toObject(GroceryItem::class.java)
                    if (item != null) {
                        // We save the document ID so we can delete it later!
                        groceryItems.add(item.copy(id = doc.id))
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
       ) {
            Text("Back to Dashboard")
        }
        Text(
            text = "Grocery List",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Logged in as: $currentUserName",
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = itemText,
            onValueChange = {
                itemText = it
                message = ""
            },
            label = { Text("Item name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = costText,
            onValueChange = {
                costText = it
                message = ""
            },
            label = { Text("Cost (€)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = currentUserName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Added by") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = dateText,
            onValueChange = {
                dateText = it
                message = ""
            },
            label = { Text("Date (e.g. DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = weekText,
            onValueChange = {
                weekText = it
                message = ""
            },
            label = { Text("Week (e.g. Week 4)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                when {
                    itemText.isBlank() -> message = "Please enter item name"
                    costText.isBlank() -> message = "Please enter cost"
                    dateText.isBlank() -> message = "Please enter date"
                    weekText.isBlank() -> message = "Please enter week"
                    else -> {
                        // Create the item object
                        val newItem = GroceryItem(
                            itemName = itemText,
                            cost = costText,
                            addedBy = currentUserName,
                            date = dateText,
                            week = weekText
                        )

                        // Save it to Firebase
                        db.collection("grocery_items")
                            .add(newItem)
                            .addOnSuccessListener {
                                itemText = ""
                                costText = ""
                                dateText = ""
                                weekText = ""
                                message = "Item successfully saved to cloud!"
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Expenses:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${String.format(java.util.Locale.getDefault(),"%.2f", totalExpenses)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }


        if (message.isNotEmpty()) {
            Text(
                text = message,
                modifier = Modifier.padding(top = 12.dp),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(groceryItems) { index, item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}. ${item.itemName}\n" +
                                    "Cost: €${item.cost}\n" +
                                    "Added by: ${item.addedBy}\n" +
                                    "Date: ${item.date}\n" +
                                    "Week: ${item.week}",
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )

                        TextButton(
                            onClick = {
                                // 1. if items has id, delete it from cloud
                                if (item.id.isNotEmpty()) {
                                        db.collection("grocery_items").document(item.id).delete()
                                    }

                                // 2. whether items has id, delete it from local
                                groceryItems.removeAt(index)
                            }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }

}