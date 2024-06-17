package com.widi.scan.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    @field:SerializedName("id") val id: Int,

    @field:SerializedName("title") val title: String,

    @field:SerializedName("content") val content: String,

    @field:SerializedName("img") val img: String,

    @field:SerializedName("reference") val url: String,

    @field:SerializedName("author") val author: String

): Parcelable