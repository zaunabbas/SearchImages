package com.payback.android.pixabay.di

import android.app.Application
import androidx.room.Room
import com.payback.android.pixabay.data.local.AppDatabase
import com.payback.android.pixabay.data.local.PixabaySearchDao
import com.payback.android.pixabay.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                Constants.DATABASE
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(appDatabase: AppDatabase): PixabaySearchDao {
        return appDatabase.pixabaySearchDao()
    }
}
