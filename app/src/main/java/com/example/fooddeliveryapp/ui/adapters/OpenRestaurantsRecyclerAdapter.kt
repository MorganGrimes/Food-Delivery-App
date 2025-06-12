package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.RecyclerRestaurantsLayoutBinding

class OpenRestaurantsRecyclerAdapter(
    private var restaurantList: List<RestaurantsItemModel>,
    private val onItemClick: (RestaurantsItemModel) -> Unit
) : RecyclerView.Adapter<OpenRestaurantsRecyclerAdapter.RestaurantsViewHolder>() {

    inner class RestaurantsViewHolder(private val binding: RecyclerRestaurantsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantsItemModel) {
            binding.apply {
                recyclerRestaurantsRestaurantIv.setImageResource(item.restaurantImage)
                recyclerRestaurantsRestaurantsNameTv.text = item.restaurantName
                recyclerRestaurantsFoodNameTv.text = item.restaurantFood
                recyclerRestaurantsRatingTv.text = item.restaurantRating
                recyclerRestaurantsDeliveryTv.text = item.restaurantDelivery
                recyclerRestaurantsDeliveryTimeTv.text = item.restaurantDeliveryTime
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val view = RecyclerRestaurantsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        holder.bind(restaurantList[position])
    }

    override fun getItemCount(): Int = restaurantList.size

    fun updateList(newList: List<RestaurantsItemModel>) {
        val diffCallback = RestaurantsDiffCallback(restaurantList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        restaurantList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class RestaurantsDiffCallback(
        private val oldList: List<RestaurantsItemModel>,
        private val newList: List<RestaurantsItemModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].restaurantName == newList[newItemPosition].restaurantName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
