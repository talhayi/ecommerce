package com.example.ecommerce.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class FilterAdapter(private val items: List<String?>) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val selectedItems = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_box_item_layout, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = items[position]
        item?.let { holder.bind(it, selectedItems.contains(item)) }
    }

    override fun getItemCount(): Int = items.size

    fun getSelectedItems(): List<String> = selectedItems.toList()

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(item: String, isSelected: Boolean) {
            checkBox.text = item
            checkBox.isChecked = isSelected
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item)
                } else {
                    selectedItems.remove(item)
                }
            }
        }
    }
}