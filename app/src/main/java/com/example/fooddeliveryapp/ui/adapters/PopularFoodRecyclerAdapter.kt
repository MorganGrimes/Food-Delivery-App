package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.databinding.RecyclerPopularFoodLayoutBinding

class PopularFoodRecyclerAdapter (
    private val items: List<PopularFoodItemModel>,
    private val onItemClick: (PopularFoodItemModel) -> Unit
) : RecyclerView.Adapter<PopularFoodRecyclerAdapter.PopularFoodViewHolder>() {

    inner class PopularFoodViewHolder(private val binding: RecyclerPopularFoodLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PopularFoodItemModel) {
            binding.apply {
                recyclerPopularFoodIv.setImageResource(item.popularFoodImage)
                recyclerPopularFoodNameTv.text = item.popularFoodName
                recyclerPopularFoodRestaurantNameTv.text = item.popularFoodRestaurantName
                recyclerPopularFoodPriceTv.text = item.popularFoodPrice

                recyclerPopularFoodSecondIv.setImageResource(item.popularFoodImage)
                recyclerPopularFoodSecondNameTv.text = item.popularFoodName
                recyclerPopularFoodSecondRestaurantNameTv.text = item.popularFoodRestaurantName
                recyclerPopularFoodSecondPriceTv.text = item.popularFoodPrice

                recyclerPopularFoodAddIv.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        val view = RecyclerPopularFoodLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularFoodViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}