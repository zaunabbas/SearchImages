package com.payback.android.pixabay.view.ui.search.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.payback.android.pixabay.R
import com.payback.android.pixabay.data.local.SearchResult
import com.payback.android.pixabay.databinding.RowSearchItemBinding
import com.payback.android.pixabay.view.ui.search.adapter.OnItemClickListener

class ImageViewHolder(private val binding: RowSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        searchResult: SearchResult?,
        listener: OnItemClickListener?
    ) {

        Glide.with(itemView.context).load(searchResult?.previewURL)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.imageView)

        binding.tvUser.text = searchResult?.user
        binding.tvTags.text = searchResult?.getTagsList()?.joinToString(" ")

        itemView.setOnClickListener {
            listener.let {
                listener?.onRowClick(bindingAdapterPosition, searchResult)
            }
        }

    }

    companion object {
        fun createViewHolder(parent: ViewGroup): ImageViewHolder {
            val itemBinding = RowSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ImageViewHolder(itemBinding)
        }
    }
}