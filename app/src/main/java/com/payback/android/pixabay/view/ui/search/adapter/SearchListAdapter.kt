package com.payback.android.pixabay.view.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.payback.android.pixabay.data.SearchResult
import com.payback.android.pixabay.view.ui.search.viewholder.ImageViewHolder

class SearchListAdapter(var historyList: ArrayList<SearchResult>) :
    ListAdapter<SearchResult, RecyclerView.ViewHolder>(SearchDiffCallback()) {

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder.createViewHolder(parent)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val history = historyList[position]
        (holder as ImageViewHolder).bind(history, listener)

    }

    fun submitRefreshList(itemList: List<SearchResult>) {
        this.historyList.clear()
        this.historyList.addAll(itemList)
        notifyDataSetChanged()
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }
}
