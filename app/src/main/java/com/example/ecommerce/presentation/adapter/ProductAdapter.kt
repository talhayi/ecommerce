package com.example.ecommerce.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ProductItemLayoutBinding
import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.presentation.products.HomeFragmentDirections

class ProductAdapter :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var onItemClickListener: ((Product) -> Unit)? = null
    private var onAddToCartClickListener: ((Product) -> Unit)? = null

    inner class ProductViewHolder(var binding: ProductItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return newItem == oldItem
        }
    }

   val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val products = differ.currentList[position]
        holder.binding.apply {
            textViewPrice.text = "${products.price} ₺"
            textViewName.text = products.name
            Glide.with(root).load(products.image).override(1000, 500).into(productImageView)

        }
        holder.binding.root.setOnClickListener {
            onItemClickListener?.let { it(products) }
        }

        holder.binding.buttonCart.setOnClickListener {
            onAddToCartClickListener?.let { it(products) }
        }
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnAddToCartClickListener(listener: (Product) -> Unit) {
        onAddToCartClickListener = listener
    }
}