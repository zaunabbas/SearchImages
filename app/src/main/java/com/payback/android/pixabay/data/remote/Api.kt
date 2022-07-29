package com.payback.android.pixabay.data.remote

import com.payback.android.pixabay.data.local.SearchResult
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

  @GET("?")
  suspend fun searchImages(
    @Query("q") query: String = "",
    @Query("page") page: Int,
  ): ApiResponse<SearchResponse<SearchResult>>
}
