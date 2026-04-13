package com.example.budgetbasket



fun formatDisplayName(name: String): String {
    return if (name.contains("@")) {
        name.substringBefore("@")
    } else {
        name
    }
}

fun getDisplayName(
    addedByUid: String,
    addedBy: String,
    userNamesByUid: Map<String, String>
): String {
    return userNamesByUid[addedByUid]
        ?: if (addedBy.contains("@")) addedBy.substringBefore("@") else addedBy
}