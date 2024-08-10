package com.example.ecommerce.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ProductItemLayoutBinding
import com.example.ecommerce.data.model.Product

class ProductAdapter : PagingDataAdapter<Product, ProductAdapter.ProductViewHolder>(diffCallBack) {

    private var onItemClickListener: ((Product) -> Unit)? = null
    private var onAddToCartClickListener: ((Product) -> Unit)? = null

    inner class ProductViewHolder(var binding: ProductItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return newItem == oldItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)?:return
        product.let {
            holder.binding.apply {
                textViewPrice.text = "${product.price} ₺"
                textViewName.text = product.name
                Glide.with(root).load(product.image).override(1000, 500).into(productImageView)
            }

            holder.binding.root.setOnClickListener {
                onItemClickListener?.let { it(product) }
            }

            holder.binding.buttonCart.setOnClickListener {
                onAddToCartClickListener?.let { it(product) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnAddToCartClickListener(listener: (Product) -> Unit) {
        onAddToCartClickListener = listener
    }
}
