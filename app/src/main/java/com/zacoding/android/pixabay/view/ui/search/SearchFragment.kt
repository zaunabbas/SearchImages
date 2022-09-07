package com.zacoding.android.pixabay.view.ui.search

import android.animation.LayoutTransition
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zacoding.android.pixabay.R
import com.zacoding.android.pixabay.data.local.SearchResult
import com.zacoding.android.pixabay.databinding.FragmentSearchBinding
import com.zacoding.android.pixabay.util.BundleConstants.Companion.IMAGE_DATA
import com.zacoding.android.pixabay.util.showConfirmationDialog
import com.zacoding.android.pixabay.util.showErrorMessageInDialog
import com.zacoding.android.pixabay.view.ui.search.adapter.OnItemClickListener
import com.zacoding.android.pixabay.view.ui.search.adapter.SearchListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    internal val searchViewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mAdapter: SearchListAdapter

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        observeData()
        observeState()
    }

    private fun initUI() {
        setupMenu()
        binding.swipe.setOnRefreshListener(this)
        initAdapter()
    }

    private fun initAdapter() {
        //initAdapter
        mAdapter = SearchListAdapter().apply {
            listener = this@SearchFragment
        }
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.list.adapter = mAdapter
        //
    }

    private fun searchImages(query: String) {
        searchViewModel.onSearchQuerySubmit(query)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            searchViewModel.searchResult.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun observeState() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mAdapter.loadStateFlow
                .distinctUntilChangedBy { it.source.refresh }
                .filter { it.source.refresh is LoadState.NotLoading }
                .collect {
                    if (searchViewModel.pendingScrollToTopAfterNewQuery) {
                        binding.list.scrollToPosition(0)
                        searchViewModel.pendingScrollToTopAfterNewQuery = false
                    }
                    if (searchViewModel.pendingScrollToTopAfterRefresh &&
                        it.mediator?.refresh is LoadState.NotLoading
                    ) {
                        binding.list.scrollToPosition(0)
                        searchViewModel.pendingScrollToTopAfterRefresh = false
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow.collect { loadState ->

                binding.swipe.isRefreshing = false
                searchViewModel.refreshInProgress = false
                binding.emptyView.clEmptyCont.isVisible = false

                when (val refresh = loadState.mediator?.refresh) {
                    is LoadState.Loading -> {
                        binding.swipe.isRefreshing = true

                        searchViewModel.refreshInProgress = true
                        searchViewModel.pendingScrollToTopAfterRefresh = true
                    }

                    is LoadState.NotLoading -> {

                        searchViewModel.newQueryInProgress = false
                    }

                    is LoadState.Error -> {

                        val noCachedResult =
                            mAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                        if (noCachedResult) {
                            binding.emptyView.clEmptyCont.isVisible = true
                            /*requireContext().showToast(
                                "Error while fetching data."
                            )*/
                        }

                        searchViewModel.newQueryInProgress = false
                        searchViewModel.pendingScrollToTopAfterRefresh = false

                    }
                    null -> {
                        // do nothing
                    }
                }

            }
        }
    }

    override fun onRowClick(position: Int, searchResult: SearchResult?) {

        requireActivity().showConfirmationDialog(
            message = getString(R.string.want_to_open_details)
        ) { dialog, _ ->
            val bundle = Bundle()
            bundle.putSerializable(IMAGE_DATA, searchResult)
            findNavController().navigate(
                R.id.action_navigation_search_to_navigation_details,
                bundle
            )
            dialog.dismiss()

        }

    }

    private fun isValidInput(query: String): Boolean {

        return when {
            TextUtils.isEmpty(
                query.trim { it <= ' ' }) -> {
                requireActivity().showErrorMessageInDialog(
                    "Validation",
                    "Please enter the search value."
                )
                false
            }
            else -> {
                true
            }
        }

    }


    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.search_menu, menu)

                val searchManager =
                    requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

                val menuItemSearch = menu.findItem(R.id.action_search)
                searchView = menuItemSearch.actionView as SearchView
                searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                personalizeSearchView()
                initSearchViewListener()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initSearchViewListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                this.callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            private fun callSearch(query: String) {
                if (isValidInput(query))
                    searchImages(query)
            }
        })
    }

    private fun personalizeSearchView() {
        val txtSearch =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = getString(R.string.search_hint)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.White_40))
        txtSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.White_100))

        // change search close button image
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_close_black_24dp)

        // Make animation transition
        val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
        searchBar.layoutTransition = LayoutTransition()
    }

    override fun onRefresh() {
        mAdapter.refresh()
    }

}