package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.databinding.RecyclerCartItemLayoutBinding
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import java.util.Locale

class CartItemRecyclerAdapter(
    private val homeViewModel: HomeViewModel,
    private var items: List<CartItemModel>,
    private var isEditMode: Boolean = false
) : RecyclerView.Adapter<CartItemRecyclerAdapter.CartViewHolder>() {

    companion object {
        private const val PAYLOAD_EDIT_MODE = "PAYLOAD_EDIT_MODE"
    }

    fun updateData(newItems: List<CartItemModel>, isEditMode: Boolean = false) {
        val oldEditMode = this.isEditMode
        this.isEditMode = isEditMode

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return oldItem.cartFoodName == newItem.cartFoodName &&
                        oldItem.restaurantId == newItem.restaurantId &&
                        oldItem.cartFoodSize == newItem.cartFoodSize
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return if (oldItem == newItem && oldEditMode != this@CartItemRecyclerAdapter.isEditMode) {
                    PAYLOAD_EDIT_MODE
                } else {
                    null
                }
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setEditMode(editMode: Boolean) {
        if (this.isEditMode != editMode) {
            this.isEditMode = editMode
            for (i in items.indices) {
                notifyItemChanged(i, PAYLOAD_EDIT_MODE)
            }
        }
    }

    inner class CartViewHolder(private val binding: RecyclerCartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItemModel) {
            binding.apply {
                recyclerCartItemRemoveItem.visibility = if (isEditMode) View.VISIBLE else View.GONE

                recyclerCartItemIv.setImageResource(item.cartImage)
                recyclerCartItemFoodTitleTv.text = item.cartFoodName
                recyclerCartItemFoodPriceTv.text =
                    String.format(Locale.getDefault(), "$%.2f", item.cartFoodPrice)
                recyclerCartItemFoodSizeTv.text = item.cartFoodSize
                recyclerCartItemNumberSelectedFoodTv.text =
                    String.format(Locale.getDefault(), "%,d", item.cartFoodQuantity)

                recyclerCartItemRemoveItem.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val updatedList = items.toMutableList()
                        updatedList.removeAt(position)
                        homeViewModel.cartItems.value = updatedList
                        updateData(updatedList, isEditMode)
                    }
                }

                recyclerCartItemPlusIconIv.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val updatedList = items.toMutableList()
                        val currentItem = updatedList[position]
                        val newQuantity = currentItem.cartFoodQuantity + 1
                        val updatedItem = currentItem.copy(
                            cartFoodQuantity = newQuantity,
                            cartFoodPrice = currentItem.cartFoodPrice / currentItem.cartFoodQuantity * newQuantity
                        )
                        updatedList[position] = updatedItem
                        homeViewModel.cartItems.value = updatedList
                        updateData(updatedList, isEditMode)
                    }
                }

                recyclerCartItemMinusIconIv.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val currentItem = items[position]
                        if (currentItem.cartFoodQuantity > 1) {
                            val updatedList = items.toMutableList()
                            val newQuantity = currentItem.cartFoodQuantity - 1
                            val updatedItem = currentItem.copy(
                                cartFoodQuantity = newQuantity,
                                cartFoodPrice = currentItem.cartFoodPrice / currentItem.cartFoodQuantity * newQuantity
                            )
                            updatedList[position] = updatedItem
                            homeViewModel.cartItems.value = updatedList
                            updateData(updatedList, isEditMode)
                        }
                    }
                }
            }
        }

        fun bindEditMode(editMode: Boolean) {
            binding.recyclerCartItemRemoveItem.visibility = if (editMode) View.VISIBLE else View.GONE
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

    override fun onBindViewHolder(holder: CartViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.bind(items[position])
        } else {
            for (payload in payloads) {
                if (payload == PAYLOAD_EDIT_MODE) {
                    holder.bindEditMode(isEditMode)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
