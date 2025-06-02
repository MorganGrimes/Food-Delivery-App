package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.address.AddressEntity
import com.example.fooddeliveryapp.databinding.RecyclerAddressLayoutBinding
import java.util.Locale

class AddressRecyclerAdapter(
    private var items: List<AddressEntity>,
    private val onEditClicked: (AddressEntity) -> Unit,
    private val onDeleteClicked: (AddressEntity) -> Unit
) : RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder>() {

    fun updateData(newItems: List<AddressEntity>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class AddressViewHolder(private val binding: RecyclerAddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressEntity) {
            binding.apply {
                recyclerAddressLabelTv.text = item.addressLabel.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                recyclerAddressAddressTv.text = item.addressName

                val iconRes = when (item.addressLabel.lowercase()) {
                    "home" -> R.drawable.home
                    "work" -> R.drawable.work
                    "other" -> R.drawable.other
                    else -> R.drawable.other
                }
                recyclerAddressIv.setImageResource(iconRes)
                recyclerAddressEditIv.setOnClickListener { onEditClicked(item) }
                recyclerAddressDeleteIv.setOnClickListener { onDeleteClicked(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = RecyclerAddressLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}