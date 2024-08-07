package com.example.ecommerce.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ProductItemLayoutBinding
import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.presentation.products.HomeFragmentDirections

class ProductAdapter(var productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var onItemClickListener: ((Product) -> Unit)? = null

    inner class ProductViewHolder(var binding: ProductItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val products = productList[position]
        holder.binding.apply {
            textViewPrice.text = "${products.price} ₺"
            textViewName.text = products.name
            Glide.with(root).load(products.image).override(1000, 500).into(productImageView)

        }
        holder.binding.root.setOnClickListener {
            onItemClickListener?.let { it(products) }
        }
   /*     holder.binding.productCardView.setOnClickListener {
            val actions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(products)
            Navigation.findNavController(it).navigate(actions)
        }*/
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }
}