// 文件：app/src/main/java/com/example/budgetbasket/GroceryRepository.kt
package com.example.budgetbasket

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class GroceryRepository {
    private val db = FirebaseFirestore.getInstance()

    /* Dynamically retrieve the current user's favorites path.
    Check the current UID in real time every time a CRUD operation is called. */
    private fun getMyCollection(): CollectionReference {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
        return db.collection("users").document(uid).collection("items")
    }

    //  Retrieve data (with added error-handling callbacks)
    fun getItems(onResult: (List<GroceryItem>, String?) -> Unit): ListenerRegistration {
        return getMyCollection().addSnapshotListener { value, error ->
            if (error != null) {
                /*
                If an error occurs (such as insufficient permissions),
                display the error message in the UI and stop the loading process.
                */
                onResult(emptyList(), error.message)
                return@addSnapshotListener
            }

            val items = value?.documents?.mapNotNull { doc ->
                doc.toObject(GroceryItem::class.java)?.copy(id = doc.id)
            } ?: emptyList()

            // Success: Data transferred; error messages set to null
            onResult(items, null)
        }
    }

    // Save data
    fun saveItem(item: GroceryItem, id: String?, onComplete: (Boolean, String?) -> Unit) {
        val collection = getMyCollection() // use the dynamic path
        val task = if (id == null) {
            collection.add(item)
        } else {
            collection.document(id).set(item)
        }

        task.addOnCompleteListener { result ->
            onComplete(result.isSuccessful, result.exception?.message)
        }
    }

    // Delete data
    fun deleteItem(id: String, onComplete: (Boolean) -> Unit) {
        getMyCollection().document(id).delete().addOnCompleteListener {
            onComplete(it.isSuccessful)
        }
    }
}