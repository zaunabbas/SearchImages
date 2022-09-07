package com.zacoding.android.pixabay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchResult::class,SearchQueryResult::class, SearchQueryRemoteKey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pixabaySearchDao(): PixabaySearchDao
    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}
