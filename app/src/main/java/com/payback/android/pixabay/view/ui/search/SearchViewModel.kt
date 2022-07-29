package com.payback.android.pixabay.view.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payback.android.pixabay.data.SearchResult
import com.payback.android.pixabay.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val searchImages: MutableStateFlow<List<SearchResult>> = MutableStateFlow(emptyList())
    private var lastQuery: String = "fruit"
    var searchPage: Long = 1
    val isLoading = MutableStateFlow(false)
    val searchError = MutableStateFlow("")

    init {
        searchImages()
    }

    fun searchImages(query: String = lastQuery) {
        if (query.isNotEmpty())
            lastQuery = query

        viewModelScope.launch {
            searchRepository.fetchImages(
                lastQuery,
                searchPage,
                onStart = { isLoading.value = true },
                onCompletion = { isLoading.value = false },
                onError = {
                    searchError.value = it
                    isLoading.value = false
                    Log.d("Error", it)
                }
            ).collectLatest {
                searchImages.value = it
            }
        }

    }

    fun searchRefresh() {
        searchPage = 1
        searchImages()
    }

}
