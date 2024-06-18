package com.widi.scan.ui.articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widi.scan.data.ScanRepository
import com.widi.scan.data.model.Article


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



