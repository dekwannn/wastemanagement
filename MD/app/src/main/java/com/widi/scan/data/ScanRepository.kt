package com.widi.scan.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.widi.scan.data.local.HistoryEntity
import com.widi.scan.data.model.Article
import com.widi.scan.data.remote.ArticlesResponse
import com.widi.scan.data.retrofit.ApiConfig
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val apiService = ApiConfig.getApiService()

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

    fun getArticles(): MutableLiveData<List<Article>?> {
        val data = MutableLiveData<List<Article>?>()

        apiService.getArticles().enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(call: Call<ArticlesResponse>, response: Response<ArticlesResponse>) {
                if (response.isSuccessful) {
                    Log.d("ArticleRepository", "Articles fetched successfully")
                    data.value = response.body()?.data
                } else {
                    Log.d("ArticleRepository", "Failed to fetch articles: ${response.code()}")
                    data.value = null
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.d("ArticleRepository", "Error: ${t.message}")
                data.value = null
            }
        })

        return data
    }

}


