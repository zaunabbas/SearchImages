package com.payback.android.pixabay.view.ui.search.adapter

import com.payback.android.pixabay.data.local.SearchResult

interface OnItemClickListener {
    fun onRowClick(
        position: Int,
        searchResult: SearchResult?
    )
}