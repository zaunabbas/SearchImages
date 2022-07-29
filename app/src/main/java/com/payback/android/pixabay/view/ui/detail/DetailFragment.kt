package com.payback.android.pixabay.view.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.payback.android.pixabay.R
import com.payback.android.pixabay.data.local.SearchResult
import com.payback.android.pixabay.databinding.FragmentDetailBinding
import com.payback.android.pixabay.util.BundleConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var searchResult: SearchResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchResult = it.getSerializable(BundleConstants.imageData) as SearchResult
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDetails()
    }

    private fun showDetails() {
        Glide.with(requireContext()).load(searchResult.largeImageURL)
            .into(binding.imageView)

        binding.tvLikesCount.text = searchResult.likes.toString()
        binding.tvCommentsCount.text = searchResult.comments.toString()
        binding.tvDownloadCount.text = searchResult.downloads.toString()
        binding.tvUser.text = searchResult.user

        searchResult.getTagsList().forEachIndexed { index, text ->
            val chip =
                layoutInflater.inflate(R.layout.row_chip_view, binding.chipGroup, false) as Chip
            chip.id = index
            chip.text = text
            binding.chipGroup.addView(chip)
        }

    }

}