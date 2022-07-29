package com.payback.android.pixabay.persistence

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.payback.android.pixabay.data.SearchResult
import com.payback.android.pixabay.data.local.AppDatabase
import com.payback.android.pixabay.data.local.PixabaySearchDao
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
    fun `query for apple`(): Unit = runBlocking {
        val mockData = SearchResult.mock()

        dao.insertSearchResult(mockData)

        val searchResultList = dao.getImagesByQuery("%apple%")
        assertThat(searchResultList.find { it.tags.contains("apple") })
    }
}