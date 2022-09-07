package com.zacoding.android.pixabay.view.ui.search.adapter

import com.zacoding.android.pixabay.data.local.SearchResult

interface OnItemClickListener {
    fun onRowClick(
        position: Int,
        searchResult: SearchResult?
    )
}