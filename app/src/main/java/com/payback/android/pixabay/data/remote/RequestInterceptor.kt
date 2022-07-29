package com.payback.android.pixabay.data.remote

import android.util.Log
import com.payback.android.pixabay.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()

        val request = originalRequest.newBuilder().url(url).build()
        Log.d("REQUEST", request.toString())
        return chain.proceed(request)
    }
}
