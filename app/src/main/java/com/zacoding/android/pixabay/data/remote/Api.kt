package com.zacoding.android.pixabay.data.remote

import com.zacoding.android.pixabay.data.local.SearchResult
import com.zacoding.android.pixabay.util.Constants.Companion.QUERY_PARAM_PAGE
import com.zacoding.android.pixabay.util.Constants.Companion.QUERY_PARAM_Q
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

  @GET("?")
  suspend fun searchImages(
    @Query(QUERY_PARAM_Q) query: String = "",
    @Query(QUERY_PARAM_PAGE) page: Int,
  ): ApiResponse<SearchResponse<SearchResult>>
}
