package com.widi.scan.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.widi.scan.data.local.HistoryEntity
import kotlinx.coroutines.tasks.await

class ScanRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllHistory(): List<HistoryEntity> {
        return getAllHistoryFromFirestore()
    }

    private suspend fun getAllHistoryFromFirestore(): List<HistoryEntity> {
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        val result = FirebaseFirestore.getInstance().collection("histories")
            .whereEqualTo("userId", user.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return result.map { document ->
            HistoryEntity(
                id = document.id,
                imageUri = document.getString("imageUri") ?: "",
                label = document.getString("label") ?: "",
                timestamp = document.getLong("timestamp") ?: 0,
                confidence = document.getLong("confidence")?.toInt() ?: 0
            )
        }
    }

    suspend fun deleteAllHistory() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val result = firestore.collection("histories")
            .whereEqualTo("userId", user.uid)
            .get()
            .await()

        result.documents.forEach { document ->
            document.reference.delete()
        }
    }

}


