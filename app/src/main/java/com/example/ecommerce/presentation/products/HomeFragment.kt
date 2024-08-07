package com.example.ecommerce.presentation.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
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
        viewModel.getProducts()
        observeProductList()

    }

    private fun observeProductList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { productState ->
                productState?.let { state ->
                    productAdapter = ProductAdapter(state.products)
                    binding.recyclerView.adapter = productAdapter
                    binding.recyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE
                    binding.textViewError.apply {
                        text = state.error
                        visibility = if (state.error != null) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }
}