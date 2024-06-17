package com.widi.scan.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.widi.scan.data.model.Article
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesResponse(
	@field:SerializedName("data") val data: List<Article>? = null,
	@field:SerializedName("message") val message: String? = null,
	@field:SerializedName("status") val status: Boolean? = null
): Parcelable

