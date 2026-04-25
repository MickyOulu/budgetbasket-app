package com.example.budgetbasket

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class GroceryRepository(private val groupId: String) {
    private val db = FirebaseFirestore.getInstance()

    private fun getGroupCollection(): CollectionReference {
        return db.collection("groups").document(groupId).collection("items")
    }

    // --- LOAD FROM FIREBASE ---
    fun getItems(onResult: (List<GroceryItem>, String?) -> Unit): ListenerRegistration {
        return getGroupCollection().addSnapshotListener { value, error ->
            if (error != null) {
                onResult(emptyList(), error.message)
                return@addSnapshotListener
            }
            val items = value?.documents?.mapNotNull { doc ->
                doc.toObject(GroceryItem::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            onResult(items, null)
        }
    }

    // --- SAVE TO FIREBASE  ---
    fun saveItem(item: GroceryItem, id: String?, onComplete: (Boolean, String?) -> Unit) {
        val collection = getGroupCollection()

        val itemWithGroup = item.copy(groupId = groupId)

        val task = if (id == null) {
            collection.add(itemWithGroup)
        } else {
            collection.document(id).set(itemWithGroup)
        }

        task.addOnCompleteListener { result ->
            onComplete(result.isSuccessful, result.exception?.message)
        }
    }

    // --- DELETE FROM FIREBASE ---
    fun deleteItem(id: String, onComplete: (Boolean) -> Unit) {
        getGroupCollection().document(id).delete().addOnCompleteListener {
            onComplete(it.isSuccessful)
        }
    }
}