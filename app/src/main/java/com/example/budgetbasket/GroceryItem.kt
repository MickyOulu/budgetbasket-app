package com.example.budgetbasket

data class GroceryItem(
    val id: String = "",
    val itemName: String = "",
    val cost: String = "",
    val addedBy: String = "",
    val addedByUid: String = "",
    val date: String = "",
    val week: String = "",
    val category: String = "Produce (Fruits & Veggies)",
    val groupId: String = ""
)