package com.example.budgetbasket


import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Calendar

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

@OptIn(ExperimentalMaterial3Api::class)
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
    var showDatePicker by remember { mutableStateOf(false) }
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var itemNameError by remember { mutableStateOf(false) }
    var costError by remember { mutableStateOf(false) }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {

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
                    itemNameError = false
                },
                label = { Text("Item name") },
                isError = itemNameError,
                supportingText = {
                    if (itemNameError) {
                        Text(text = "Item name cannot be empty", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = costText,
                onValueChange = {
                    costText = it
                    costError = false
                },
                label = { Text("Cost (€)") },
                isError = costError,
                supportingText = {
                    if (costError) {
                        Text(text = "Please enter a valid price (e.g., 2.50)", color = MaterialTheme.colorScheme.error)
                    }
                },
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker = true
                        message = ""
                    }
            ) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Select Date") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weekText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Week") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                Button(
                    onClick = {

                        itemNameError = itemText.isBlank()
                        costError = costText.isBlank() || costText.toDoubleOrNull() == null

                        if(itemNameError || costError) {
                            message = "Please correct the errors above."
                            return@Button
                        }

                        val itemData = GroceryItem(
                            itemName = itemText,
                            cost = costText,
                            addedBy = currentUserName,
                            date = dateText,
                            week = weekText
                        )

                        if (editingItemId == null) {
                            db.collection("grocery_items").add(itemData)
                        } else {
                            db.collection("grocery_items").document(editingItemId!!).set(itemData)
                            editingItemId = null
                        }

                        itemText = ""
                        costText = ""
                        dateText = ""
                        weekText = ""
                        message = "Item saved successfully"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (editingItemId == null) "Add Item" else "Update Item")
                }

                if (editingItemId != null) {
                    Button(
                        onClick = {
                            editingItemId = null
                            itemText = ""; costText = ""; dateText = ""; weekText = ""; message = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }

        if (showDatePicker) {
            item {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = System.currentTimeMillis(),
                    selectableDates = object : androidx.compose.material3.SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis <= System.currentTimeMillis()
                        }
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val selectedMillis = datePickerState.selectedDateMillis
                                if (selectedMillis != null) {
                                    val selectedDate = Date(selectedMillis)
                                    val formatter =
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    dateText = formatter.format(Date(selectedMillis))

                                    val calendar = Calendar.getInstance()
                                    calendar.time = selectedDate
                                    val weekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
                                    weekText = "Week $weekNumber"
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        item {
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
                        text = "€${
                            String.format(
                                Locale.getDefault(),
                                "%.2f",
                                totalExpenses
                            )
                        }",
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
            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(24.dp))
        }

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
                            editingItemId = item.id
                            itemText = item.itemName
                            costText = item.cost
                            dateText = item.date
                            weekText = item.week
                            message = "Editing: ${item.itemName}"
                        }
                    ) {
                        Text("Edit")
                    }

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
