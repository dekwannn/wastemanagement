package com.widi.scan.data.retrofit

import com.widi.scan.data.remote.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/article")
    fun getArticles(): Call<ArticlesResponse>
}