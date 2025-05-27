package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.FoodItemModel
import com.example.fooddeliveryapp.databinding.RecyclerFoodLayoutBinding

class FoodRecyclerAdapter(
    private val items: List<FoodItemModel>,
    private val onItemClick: (FoodItemModel) -> Unit
) : RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(private val binding: RecyclerFoodLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodItemModel) {
            binding.apply {
                recyclerFoodNameBtn.text = item.foodName
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = RecyclerFoodLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}