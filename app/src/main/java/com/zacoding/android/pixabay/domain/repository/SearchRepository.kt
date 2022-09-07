package com.zacoding.android.pixabay.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zacoding.android.pixabay.data.SearchNewsRemoteMediator
import com.zacoding.android.pixabay.data.local.AppDatabase
import com.zacoding.android.pixabay.data.local.PixabaySearchDao
import com.zacoding.android.pixabay.data.local.SearchResult
import com.zacoding.android.pixabay.data.remote.Api
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: Api,
    private val pixabaySearchDb: AppDatabase
) {
    private val pixabaySearchDao: PixabaySearchDao = pixabaySearchDb.pixabaySearchDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResultPaged(
        query: String,
        refreshOnInit: Boolean
    ): Flow<PagingData<SearchResult>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = SearchNewsRemoteMediator(
                query,
                apiService,
                pixabaySearchDb,
                refreshOnInit
            ),
            pagingSourceFactory = { pixabaySearchDao.getSearchResultsByQueryPaged(query) }
        ).flow

}