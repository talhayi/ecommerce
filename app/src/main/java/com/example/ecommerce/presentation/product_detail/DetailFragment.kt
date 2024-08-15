package com.example.ecommerce.presentation.product_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentDetailBinding
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDetail()
        onBackButton()
        addToCart()
        observeBadgeItemCount()
    }

    private fun showDetail() {
        val args: DetailFragmentArgs by navArgs()
        product = args.product
        binding.apply {
            textViewToolBar.text = product.name
            textViewName.text = product.name
            textViewDescription.text = product.description
            textViewPrice.text = "${product.price} ₺"
            Glide.with(this@DetailFragment).load(product.image).override(1000, 500)
                .into(imageViewDetail)
        }
    }

    private fun onBackButton() {
        binding.imageViewBack.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_detailFragment_to_homeFragment)
        }
    }

    private fun addToCart() {
        binding.buttonCart.setOnClickListener {
            viewModel.saveCart(product)
        }
    }

    private fun observeBadgeItemCount() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.badgeCount.collect { badgeCount ->
                (activity as? MainActivity)?.cartBadge(badgeCount)
            }
        }
    }
}
