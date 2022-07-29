package com.payback.android.pixabay.data.local

import androidx.room.*
import com.payback.android.pixabay.data.SearchResult
import kotlinx.coroutines.flow.Flow

@Dao
interface PixabaySearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResult(searchResult: List<SearchResult>)

    @Query("SELECT * FROM SearchResult WHERE tags LIKE :query")
    suspend fun getImagesByQuery(query: String): List<SearchResult>
}
