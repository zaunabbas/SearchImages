package com.zacoding.android.pixabay.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabaySearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResult(searchResult: List<SearchResult>)

    @Query("SELECT * FROM search_query_results INNER JOIN search_results ON searchedDataId = id WHERE searchQuery = :query ORDER BY queryPosition")
    fun getSearchResultsByQueryPaged(query: String): PagingSource<Int, SearchResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQueryResult(searchQueryResult: List<SearchQueryResult>)

    @Query("DELETE FROM search_query_results WHERE searchQuery = :searchQuery")
    suspend fun deleteSearchResultForQuery(searchQuery: String)

    @Query("SELECT MAX(queryPosition) FROM search_query_results WHERE searchQuery = :searchQuery")
    suspend fun getLastQueryPosition(searchQuery: String): Int?

    @Query("SELECT * FROM search_query_results INNER JOIN search_results ON searchedDataId = id WHERE searchQuery = :query ORDER BY queryPosition")
    fun getSearchResultsByQueryList(query: String): List<SearchResult>
}
