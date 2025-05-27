package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.CategoriesItemModel
import com.example.fooddeliveryapp.databinding.RecyclerCategoriesLayoutBinding

class CategoriesRecyclerAdapter(
    private val items: List<CategoriesItemModel>,
    private val onItemClick: (CategoriesItemModel) -> Unit
) : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(private val binding: RecyclerCategoriesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoriesItemModel) {
            binding.apply {
                recyclerCategoriesIv.setImageResource(item.foodImage)
                recyclerCategoriesName.text = item.foodName
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = RecyclerCategoriesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}