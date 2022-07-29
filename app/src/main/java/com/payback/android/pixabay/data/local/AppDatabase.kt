package com.payback.android.pixabay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.payback.android.pixabay.data.SearchResult

@Database(entities = [SearchResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun pixabaySearchDao(): PixabaySearchDao
}
