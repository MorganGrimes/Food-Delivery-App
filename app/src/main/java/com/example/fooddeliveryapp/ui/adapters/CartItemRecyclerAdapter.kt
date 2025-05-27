package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.databinding.RecyclerCartItemLayoutBinding

class CartItemRecyclerAdapter(
    private val items: List<CartItemModel>,
) : RecyclerView.Adapter<CartItemRecyclerAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: RecyclerCartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItemModel) {
            binding.apply {
                recyclerCartItemIv.setImageResource(item.cartImage)
                recyclerCartItemFoodTitleTv.text = item.cartFoodName
                recyclerCartItemFoodPriceTv.text = item.cartFoodPrice
                recyclerCartItemFoodSizeTv.text = item.cartFoodSize
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = RecyclerCartItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}