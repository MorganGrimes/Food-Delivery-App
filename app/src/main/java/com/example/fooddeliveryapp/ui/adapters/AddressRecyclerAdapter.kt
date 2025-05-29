package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.AddressItemModel
import com.example.fooddeliveryapp.databinding.RecyclerAddressLayoutBinding

class AddressRecyclerAdapter(
    private val items: List<AddressItemModel>,
) : RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(private val binding: RecyclerAddressLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressItemModel) {
            binding.apply {
                recyclerAddressIv.setImageResource(item.addressImage)
                recyclerAddressLabelTv.text = item.addressLabel
                recyclerAddressAddressTv.text = item.addressName
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