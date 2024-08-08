package com.example.ecommerce.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce.R
import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.presentation.adapter.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCartList()
        setupRecyclerView()
        decreaseQuantity()
        increaseQuantity()
        observeCartList()
        onItemClick()
        clearCart()
        observeTotalPrice()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter()
        binding.recyclerViewCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeCartList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartState.collectLatest { cartState ->
                Log.d("CartFragment", "Collecting cart state: $cartState")
                cartState?.let { state ->
                    state.products.forEach { product ->
                        Log.d(
                            "CartFragment",
                            "Product ID: ${product.id}, Quantity: ${product.quantity}"
                        )
                    }
                    cartAdapter.differ.submitList(state.products)
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

    private fun onItemClick() {
        cartAdapter.setOnItemClickListener { product ->
            val bundle = Bundle().apply {
                putSerializable(getString(R.string.product), product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_detailFragment, bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeTotalPrice() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collect { price ->
                binding.textViewTotalPrice.text = "$price ₺"
            }
        }
    }
    private fun increaseQuantity() {
        cartAdapter.setOnIncreaseClickListener { product ->
            val currentQuantity = product.quantity ?: 0
            val newQuantity = currentQuantity + 1
            viewModel.updateQuantity(product.id!!, newQuantity)
            observeTotalPrice()
        }
    }
    private fun decreaseQuantity() {
        cartAdapter.setOnDecreaseClickListener { product ->
            if (product.quantity == 1) {
                viewModel.deleteCart(product)
                observeTotalPrice()
            }else{
                val currentQuantity = product.quantity ?: 0
                val newQuantity = (currentQuantity - 1).coerceAtLeast(1)
                viewModel.updateQuantity(product.id!!, newQuantity)
            }
            observeTotalPrice()
        }
    }

    private fun clearCart(){
        binding.buttonComplete.setOnClickListener {
            viewModel.clearCart()
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
    }
}
