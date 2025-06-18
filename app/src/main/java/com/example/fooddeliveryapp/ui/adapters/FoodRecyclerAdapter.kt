package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.FoodItemModel
import com.example.fooddeliveryapp.databinding.RecyclerFoodLayoutBinding

class FoodRecyclerAdapter(
    private var items: List<FoodItemModel>,
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
                recyclerFoodNameBtn.setOnClickListener {
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


    fun updateList(newItems: List<FoodItemModel>) {
        val diffCallback = FoodDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class FoodDiffCallback(
        private val oldList: List<FoodItemModel>,
        private val newList: List<FoodItemModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].foodName == newList[newItemPosition].foodName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}