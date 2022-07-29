package com.payback.android.pixabay.persistence

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.payback.android.pixabay.data.local.AppDatabase
import com.payback.android.pixabay.data.local.PixabaySearchDao
import com.payback.android.pixabay.data.local.SearchQueryResult
import com.payback.android.pixabay.data.local.SearchResult
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PixabaySearchDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: PixabaySearchDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.pixabaySearchDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun query_for_apple(): Unit = runBlocking {
        val mockData = SearchResult.mock()
        val searchQuery = ""
        var queryPosition = 1
        val searchQueryResult = mockData.map { mockSearch ->
            SearchQueryResult(
                searchQuery = searchQuery,
                searchedDataId = mockSearch.id,
                queryPosition++
            )
        }

        dao.insertSearchResult(mockData)
        dao.insertSearchQueryResult(searchQueryResult)

        val searchResultList = dao.getSearchResultsByQueryList(searchQuery)
        assertThat(searchResultList.find { it.tags.contains(searchQuery) })
    }
}