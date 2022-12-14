package com.zacoding.android.pixabay.data.remote

import android.util.Log
import com.zacoding.android.pixabay.BuildConfig
import com.zacoding.android.pixabay.util.Constants.Companion.QUERY_PARAM_KEY
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(QUERY_PARAM_KEY, BuildConfig.API_KEY)
            .build()

        val request = originalRequest.newBuilder().url(url).build()
        Log.d("REQUEST", request.toString())
        return chain.proceed(request)
    }
}
