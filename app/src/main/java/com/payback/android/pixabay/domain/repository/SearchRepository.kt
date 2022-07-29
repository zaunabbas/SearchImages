package com.payback.android.pixabay.domain.repository

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.payback.android.pixabay.data.SearchErrorResponse
import com.payback.android.pixabay.data.local.PixabaySearchDao
import com.payback.android.pixabay.data.remote.Api
import com.payback.android.pixabay.util.isConnected
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: Api,
    private val pixabaySearchDao: PixabaySearchDao,
    private val application: Application
) {
    @WorkerThread
    fun fetchImages(
        query: String,
        page: Long,
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {

        val isInternetAvailable = application.isConnected
        Log.d("NetworkStarts", isInternetAvailable.toString())

        if (isInternetAvailable) {
            // request API network call asynchronously.
            apiService.searchImages(query, page.toInt())
                // handle the case when the API request gets a success response.
                .suspendOnSuccess {
                    Log.d("Result", data.toString())
                    pixabaySearchDao.insertSearchResult(data.result)
                    emit(data.result)
                }
                // handle the case when the API request is fails.
                // e.g. internal server error.
                .suspendOnError {
                    var errorMessage = message()
                    errorBody?.let {
                        val errorResponse: SearchErrorResponse =
                            Gson().fromJson(it.string(), SearchErrorResponse::class.java)
                        errorMessage = errorResponse.error
                    }
                    Log.d("suspendOnError", errorMessage)
                    onError(errorMessage)
                }
        } else {
            emit(getImagesByQuery("%$query%"))
        }

    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)

    private suspend fun getImagesByQuery(query: String) = pixabaySearchDao.getImagesByQuery(query)

}