package com.example.yelpapipractice.feature.yelp.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpapipractice.databinding.FragmentYelpBinding
import com.example.yelpapipractice.feature.yelp.presentation.viewmodel.YelpViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class YelpFragment : Fragment() {

    private var _binding: FragmentYelpBinding? = null

    private val binding get() = _binding!!
    private val viewModel: YelpViewModel by viewModels()

    @Inject
    lateinit var yelpAdapter: YelpAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentYelpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupListener()
        setupRecyclerView()
    }

    private fun setupObserver() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { businesses ->
                yelpAdapter.setBusinessList(businesses)

            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isRefreshing.flowWithLifecycle(lifecycle).onEach { isRefreshing ->
            binding.swipeContainer.isRefreshing = isRefreshing
        }.launchIn(lifecycleScope)

        viewModel.showError.flowWithLifecycle(lifecycle).onEach { hasError ->
            if (hasError) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }.launchIn(lifecycleScope)
    }

    private fun setupListener() {
        yelpAdapter.setOnItemClick {

        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshBusinesses()
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerView.apply {
                setHasFixedSize(true)// performance improvement
                layoutManager = LinearLayoutManager(requireContext())
                adapter = yelpAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}