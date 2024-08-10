package com.example.ecommerce.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.CartItemLayoutBinding
import com.example.ecommerce.data.model.Product

class CartAdapter() :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private var onItemClickListener: ((Product) -> Unit)? = null
    private var onIncreaseClickListener: ((Product) -> Unit)? = null
    private var onDecreaseClickListener: ((Product) -> Unit)? = null

    inner class CartViewHolder(var binding: CartItemLayoutBinding) :
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            textViewPrice.text = "${product.price} ₺"
            textViewName.text = product.name
            textViewQuantity.text = product.quantity.toString()
            root.setOnClickListener {
                onItemClickListener?.let { it(product) }
            }

            buttonIncrease.setOnClickListener {
                onIncreaseClickListener?.let {
                    it(product)
                    notifyItemChanged(position)
                }
            }

            buttonDecrease.setOnClickListener {
                onDecreaseClickListener?.let {
                    it(product)
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnIncreaseClickListener(listener: (Product) -> Unit) {
        onIncreaseClickListener = listener
    }

    fun setOnDecreaseClickListener(listener: (Product) -> Unit) {
        onDecreaseClickListener = listener
    }
}