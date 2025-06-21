package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.local.entity.OrderEntity
import com.example.fooddeliveryapp.databinding.RecyclerOrderLayoutBinding

class OrderRecyclerAdapter(
    private var items: List<OrderEntity>,
) : RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

    fun updateData(newItems: List<OrderEntity>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].orderId == newItems[newItemPosition].orderId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class OrderViewHolder(private val binding: RecyclerOrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderEntity) {
            binding.apply {
                recyclerOrderCategoryTypeNameTv.text = item.orderCategoryTypeName
                recyclerOrderStatusOrderTv.text = item.orderStatus
                recyclerOrderFoodIv.setImageResource(item.orderImage)
                recyclerOrderSellerNameTv.text = item.orderSellerName
                recyclerOrderPriceTv.text = item.orderPrice
                recyclerOrderDateOrderTv.text = item.orderDate
                recyclerOrderItemNumberTv.text = item.orderItemNumber
                recyclerOrderIdOrderTv.text = item.orderId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = RecyclerOrderLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}