package com.example.ecommerce.presentation.products

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ecommerce.R
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
        setupRecyclerView()
        observeProductList()
        onItemClick()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeProductList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { productState ->
                productState?.let { state ->
                    productAdapter.productList = state.products
                    productAdapter.notifyDataSetChanged()
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

    private fun onItemClick(){
        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.product), it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        productAdapter.setOnItemClickListener { product ->
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.product), product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
}