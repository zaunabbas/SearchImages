package com.payback.android.pixabay.view.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.payback.android.pixabay.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow("")

    private var refreshOnInit = false

    init {
        onSearchQuerySubmit("fruits")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResult = currentQuery.flatMapLatest { query ->
        query.let {
            searchRepository.getSearchResultPaged(query, refreshOnInit)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    var refreshInProgress = false // boolean to Snackbar in SearchNews Fragment
    var pendingScrollToTopAfterRefresh = false
    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false

    fun onSearchQuerySubmit(queryString: String) {
        refreshOnInit = true
        currentQuery.value = queryString
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
    }

}
