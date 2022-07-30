package com.payback.android.pixabay.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.payback.android.pixabay.data.local.AppDatabase
import com.payback.android.pixabay.data.local.SearchQueryRemoteKey
import com.payback.android.pixabay.data.local.SearchQueryResult
import com.payback.android.pixabay.data.local.SearchResult
import com.payback.android.pixabay.data.remote.Api
import com.skydoves.sandwich.*
import retrofit2.HttpException
import java.io.IOException

private const val NEWS_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SearchNewsRemoteMediator(
    private val searchQuery: String,
    private val pixabaySearchApi: Api,
    private val pixabaySearchDb: AppDatabase,
    private val refreshOnInit: Boolean
) : RemoteMediator<Int, SearchResult>() {

    private val pixabaySearchDao = pixabaySearchDb.pixabaySearchDao()
    private val searchQueryRemoteKeyDao = pixabaySearchDb.searchQueryRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchResult>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> NEWS_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> searchQueryRemoteKeyDao.getRemoteKey(searchQuery).nextPageKey
        }

        try {
            val response = pixabaySearchApi.searchImages(searchQuery, page)
            val serverSearchResult = arrayListOf<SearchResult>()
            response
                .onSuccess {
                    serverSearchResult.addAll(data.result)
                }.onFailure {
                    throw IOException(message())
                }.onError { throw IOException(message()) }

            pixabaySearchDb.withTransaction {
                if (loadType == LoadType.REFRESH && serverSearchResult.isNotEmpty()) {
                    pixabaySearchDao.deleteSearchResultForQuery(searchQuery)
                }

                val lastQueryPosition = pixabaySearchDao.getLastQueryPosition(searchQuery) ?: 0
                var queryPosition = lastQueryPosition + 1

                val searchQueryResult = serverSearchResult.map { serverSearchResult ->
                    SearchQueryResult(
                        searchQuery = searchQuery,
                        searchedDataId = serverSearchResult.id,
                        queryPosition++
                    )
                }

                val nextPageKey = page + 1

                pixabaySearchDao.insertSearchResult(serverSearchResult)
                pixabaySearchDao.insertSearchQueryResult(searchQueryResult)
                searchQueryRemoteKeyDao.insertRemoteKey(
                    SearchQueryRemoteKey(searchQuery, nextPageKey)
                )
            }
            return MediatorResult.Success(endOfPaginationReached = serverSearchResult.isEmpty())
        } catch (ex: IOException) {
            return MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            return MediatorResult.Error(ex)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return if (refreshOnInit) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
}