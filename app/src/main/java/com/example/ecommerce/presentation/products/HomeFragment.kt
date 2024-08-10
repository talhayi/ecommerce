package com.example.ecommerce.presentation.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.presentation.adapter.ProductAdapter
import com.example.ecommerce.presentation.cart.CartViewModel
import com.example.ecommerce.presentation.filter.FilterOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getProducts()
        setupRecyclerView()
        onItemClick()
        addToCart()
        search()
        filter()
        observeProductList()
    }

    private fun observeProductList() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.products.collectLatest { state ->
                if (state.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    state.products.let {
                        productAdapter.submitData(it)
                    }
                }

                state.error?.let {
                    binding.textViewError.text = it
                    binding.textViewError.visibility = View.VISIBLE
                } ?: run {
                    binding.textViewError.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            productAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.visibility =
                    if (loadStates.refresh is LoadState.Loading ||
                        loadStates.append is LoadState.Loading
                    ) View.VISIBLE else View.GONE

                val errorState = loadStates.source.append as? LoadState.Error
                    ?: loadStates.source.prepend as? LoadState.Error
                    ?: loadStates.refresh as? LoadState.Error

                binding.textViewError.apply {
                    text = errorState?.error?.localizedMessage
                    visibility = if (errorState != null) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun onItemClick() {
        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.product), it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
    private fun addToCart() {
        productAdapter.setOnAddToCartClickListener {
            cartViewModel.saveCart(it)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun search() {
        var job: Job? = null
        binding.editTextSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    homeViewModel.setFilterOptions(FilterOptions(name = it))
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(1000L)
                        homeViewModel.setFilterOptions(FilterOptions(name = it))
                    }
                }
                return true
            }
        })
    }

    private fun filter() {
        binding.buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_filterFragment)
        }
    }
}