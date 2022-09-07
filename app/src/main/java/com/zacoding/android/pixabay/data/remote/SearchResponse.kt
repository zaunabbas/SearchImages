package com.zacoding.android.pixabay.data.remote

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(
    val total: Long,
    val totalHits: Long,
    @SerializedName("hits")
    val result: List<T>
)

data class SearchErrorResponse(
    @SerializedName("error_code")
    val errorCode: Int,
    val error: String
)
