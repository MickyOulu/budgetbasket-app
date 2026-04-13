package com.example.budgetbasket

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class GroceryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("grocery_items")

    fun getItems(onResult: (List<GroceryItem>) -> Unit): ListenerRegistration {
        return collection.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            val items = value?.documents?.mapNotNull { doc ->
                doc.toObject(GroceryItem::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            onResult(items)
        }
    }

    fun saveItem(item: GroceryItem, id: String?, onComplete: (Boolean, String?) -> Unit) {
        val task = if (id == null) {
            collection.add(item)
        } else {
            collection.document(id).set(item)
        }

        task.addOnCompleteListener { result ->
            onComplete(result.isSuccessful, result.exception?.message)
        }
    }

    fun deleteItem(id: String, onComplete: (Boolean) -> Unit) {
        collection.document(id).delete().addOnCompleteListener {
            onComplete(it.isSuccessful)
        }
    }
}