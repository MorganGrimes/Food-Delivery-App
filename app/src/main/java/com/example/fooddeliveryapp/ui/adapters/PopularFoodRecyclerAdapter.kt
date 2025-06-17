package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.databinding.RecyclerPopularFoodLayoutBinding

class PopularFoodRecyclerAdapter(
    private var foodList: List<PopularFoodItemModel>,
    private val onItemClick: (PopularFoodItemModel) -> Unit
) : RecyclerView.Adapter<PopularFoodRecyclerAdapter.PopularFoodViewHolder>() {

    inner class PopularFoodViewHolder(private val binding: RecyclerPopularFoodLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leftItem: PopularFoodItemModel, rightItem: PopularFoodItemModel?) {

            binding.recyclerPopularFoodIv.setImageResource(leftItem.popularFoodImage)
            binding.recyclerPopularFoodNameTv.text = leftItem.popularFoodName
            binding.recyclerPopularFoodRestaurantNameTv.text = leftItem.popularFoodRestaurantName
            binding.recyclerPopularFoodPriceTv.text = leftItem.popularFoodPrice

            binding.ll2.setOnClickListener {
                onItemClick(leftItem)
            }

            binding.recyclerPopularFoodAddIv.setOnClickListener {
                onItemClick(leftItem)
            }

            if (rightItem != null) {
                binding.ll3.visibility = View.VISIBLE

                binding.recyclerPopularFoodSecondIv.setImageResource(rightItem.popularFoodImage)
                binding.recyclerPopularFoodSecondNameTv.text = rightItem.popularFoodName
                binding.recyclerPopularFoodSecondRestaurantNameTv.text = rightItem.popularFoodRestaurantName
                binding.recyclerPopularFoodSecondPriceTv.text = rightItem.popularFoodPrice

                binding.ll3.setOnClickListener {
                    onItemClick(rightItem)
                }

                binding.recyclerPopularFoodSecondAddIv.setOnClickListener {
                    onItemClick(rightItem)
                }

            } else {
                binding.ll3.visibility = View.INVISIBLE
                binding.ll3.setOnClickListener(null)
                binding.recyclerPopularFoodSecondAddIv.setOnClickListener(null)
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
        val leftItem = foodList[position * 2]
        val rightItem = if (position * 2 + 1 < foodList.size) foodList[position * 2 + 1] else null
        holder.bind(leftItem, rightItem)
    }

    override fun getItemCount(): Int = (foodList.size + 1) / 2

    fun updateList(newList: List<PopularFoodItemModel>) {
        val diffCallback = PopularFoodDiffCallback(foodList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        foodList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class PopularFoodDiffCallback(
        private val oldList: List<PopularFoodItemModel>,
        private val newList: List<PopularFoodItemModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].popularFoodName == newList[newItemPosition].popularFoodName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
