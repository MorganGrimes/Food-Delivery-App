package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.OrderItemModel
import com.example.fooddeliveryapp.databinding.RecyclerOrderLayoutBinding

class OrderRecyclerAdapter(
    private val items: List<OrderItemModel>,
) : RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: RecyclerOrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItemModel) {
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