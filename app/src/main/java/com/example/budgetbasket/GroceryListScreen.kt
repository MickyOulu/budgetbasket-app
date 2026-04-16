package com.example.budgetbasket

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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


data class GroceryItem(
    val id: String = "",
    val itemName: String = "",
    val cost: String = "",
    val addedBy: String = "",
    val date: String = "",
    val week: String = "",
    val category: String = "Food"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(
    currentUserName: String,
    onBackClick: () -> Unit,
) {
    val repository = remember { GroceryRepository() }
    var itemText by remember { mutableStateOf("") }
    var costText by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var weekText by remember { mutableStateOf("") }

    var categoryText by remember { mutableStateOf("Food") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    val categories = listOf(
        "Produce",
        "Meat & Seafood",
        "Dairy & Eggs",
        "Pantry & Grains",
        "Snacks & Drinks",
        "Household",
        "Personal Care"
    )

    var message by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var itemNameError by remember { mutableStateOf(false) }
    var costError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val groceryItems = remember { mutableStateListOf<GroceryItem>() }

    val totalExpenses = groceryItems.sumOf { item ->
        item.cost.toDoubleOrNull() ?: 0.0
    }

    LaunchedEffect(Unit) {
        isLoading = true
        repository.getItems { items ->
            isLoading = false
            groceryItems.clear()
            groceryItems.addAll(items)
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Box(modifier = Modifier.fillMaxSize()) {
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
                            Text(
                                text = "Item name cannot be empty",
                                color = MaterialTheme.colorScheme.error
                            )
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
                            Text(
                                text = "Please enter a valid price (e.g., 2.50)",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = categoryText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = {
                            Text(
                                text = if (showCategoryMenu) "▲" else "▼",
                                modifier = Modifier.clickable { showCategoryMenu = true }.padding(12.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().clickable { showCategoryMenu = true }
                    )

                    androidx.compose.material3.DropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        categories.forEach { label ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    categoryText = label
                                    showCategoryMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    Button(
                        onClick = {
                            itemNameError = itemText.isBlank()
                            costError = costText.isBlank() || costText.toDoubleOrNull() == null

                            if (itemNameError || costError) {
                                message = "Please correct the errors above."
                                return@Button
                            }

                            isLoading = true

                            val itemData = GroceryItem(
                                itemName = itemText,
                                cost = costText,
                                addedBy = currentUserName,
                                date = dateText,
                                week = weekText,
                                category = categoryText
                            )

                            repository.saveItem(itemData, editingItemId) { success, errorMsg ->

                                isLoading = false
                                if (success) {
                                    editingItemId = null
                                    itemText = ""
                                    costText = ""
                                    dateText = ""
                                    weekText = ""
                                    categoryText = "Food"
                                    message = "Item saved successfully"
                                } else {

                                    message = "Error: $errorMsg"
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (editingItemId == null) "Add Item" else "Update Item")
                    }

                    if (editingItemId != null) {
                        Button(
                            onClick = {
                                editingItemId = null
                                itemText = ""; costText = ""; dateText = ""; weekText =
                                ""; message = ""
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
                Spacer(modifier = Modifier.height(36.dp))

            }

            itemsIndexed(groceryItems) { index, item ->
                GroceryItemRow(
                    index = index,
                    item = item,
                    onEdit = {
                        editingItemId = item.id
                        itemText = item.itemName
                        costText = item.cost
                        dateText = item.date
                        weekText = item.week
                        categoryText = item.category
                        message = "Editing: ${item.itemName}"
                    },
                    onRemove = {
                        if (item.id.isNotEmpty()) {
                            isLoading = true
                            repository.deleteItem(item.id) { success ->
                                isLoading = false
                                if (!success) {
                                    message = "Error deleting item"
                                }
                            }
                        }
                    }
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun GroceryItemRow(
    index: Int,
    item: GroceryItem,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${index + 1}. ${item.itemName}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Category: ${item.category}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "Added by: ${item.addedBy} • ${item.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = "€${item.cost}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Row {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onRemove) { Text("Remove", color = Color.Red) }
            }
        }
    }
}
