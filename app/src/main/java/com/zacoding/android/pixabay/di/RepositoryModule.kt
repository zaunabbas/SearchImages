package com.zacoding.android.pixabay.di

import com.zacoding.android.pixabay.data.local.AppDatabase
import com.zacoding.android.pixabay.data.remote.Api
import com.zacoding.android.pixabay.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        api: Api,
        pixabaySearchDb: AppDatabase,
    ): SearchRepository {
        return SearchRepository(api, pixabaySearchDb)
    }
}
