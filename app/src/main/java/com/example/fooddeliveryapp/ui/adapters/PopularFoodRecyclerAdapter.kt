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
            binding.apply {
                recyclerPopularFoodIv.setImageResource(leftItem.popularFoodImage)
                recyclerPopularFoodNameTv.text = leftItem.popularFoodName
                recyclerPopularFoodRestaurantNameTv.text =
                    leftItem.popularFoodRestaurantName
                recyclerPopularFoodPriceTv.text = leftItem.popularFoodPrice

                ll2.setOnClickListener {
                    onItemClick(leftItem)
                }

                recyclerPopularFoodAddIv.setOnClickListener {
                    onItemClick(leftItem)
                }

                if (rightItem != null) {
                    ll3.visibility = View.VISIBLE

                    recyclerPopularFoodSecondIv.setImageResource(rightItem.popularFoodImage)
                    recyclerPopularFoodSecondNameTv.text = rightItem.popularFoodName
                    recyclerPopularFoodSecondRestaurantNameTv.text =
                        rightItem.popularFoodRestaurantName
                    recyclerPopularFoodSecondPriceTv.text = rightItem.popularFoodPrice

                    ll3.setOnClickListener {
                        onItemClick(rightItem)
                    }

                    recyclerPopularFoodSecondAddIv.setOnClickListener {
                        onItemClick(rightItem)
                    }

                } else {
                    ll3.visibility = View.INVISIBLE
                    ll3.setOnClickListener(null)
                    recyclerPopularFoodSecondAddIv.setOnClickListener(null)
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
