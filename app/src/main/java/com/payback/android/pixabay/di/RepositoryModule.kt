package com.payback.android.pixabay.di

import android.app.Application
import com.payback.android.pixabay.data.remote.Api
import com.payback.android.pixabay.data.local.PixabaySearchDao
import com.payback.android.pixabay.domain.repository.SearchRepository
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
        pixabaySearchDao: PixabaySearchDao,
        application: Application
    ): SearchRepository {
        return SearchRepository(api, pixabaySearchDao, application)
    }
}
