package com.payback.android.pixabay.di

import com.payback.android.pixabay.BuildConfig
import com.payback.android.pixabay.data.remote.RequestInterceptor
import com.payback.android.pixabay.data.remote.Api
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val timeout: Int = 2

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(timeout.toLong(), TimeUnit.MINUTES)
            .connectTimeout(timeout.toLong(), TimeUnit.MINUTES)
            .readTimeout(timeout.toLong(), TimeUnit.MINUTES)
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}
