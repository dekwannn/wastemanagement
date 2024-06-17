package com.widi.scan.ui.articles


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.widi.scan.data.ScanRepository
import com.widi.scan.data.model.Article
import com.widi.scan.data.remote.ArticlesResponse
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: ScanRepository) : ViewModel() {
    val loading = MutableLiveData<Boolean>()

    fun getArticles(): MutableLiveData<List<Article>?> {
        loading.postValue(true)
        val articlesLiveData = repository.getArticles()
        articlesLiveData.observeForever {
            loading.postValue(false)
        }
        return articlesLiveData
    }
}



